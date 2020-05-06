package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.OpenManuscriptReviewRequest;
import fudan.se.lab2.controller.request.ShowSubmissionRequest;
import fudan.se.lab2.controller.response.AllConferenceResponse;
import fudan.se.lab2.controller.response.ConferenceForChairResponse;
import fudan.se.lab2.controller.response.ShowSubmissionResponse;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
public class MyRelatedConferenceService {
    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private PCMemberRepository pcMemberRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    public List<ConferenceForChairResponse> showAllConferenceForChair(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long id = userRepository.findByUsername(username).getId();
        List<Conference> conferences = conferenceRepository.findAllByChairId(id);
        List<ConferenceForChairResponse> conferenceForChairResponses = new ArrayList<>();
        for (Conference conference : conferences) {
            ConferenceForChairResponse conferenceForChairResponse = new ConferenceForChairResponse();
            conferenceForChairResponse.setConference(conference);
            List<Article> articles = articleRepository.findByConferenceID(conference.getId());
            int flag =1;
            for (Article article : articles) {
                if (article.getStatus() == 0){
                    flag = 0;
                    break;
                }
            }
            conferenceForChairResponse.setCanRelease(flag);
            conferenceForChairResponses.add(conferenceForChairResponse);
        }
        return conferenceForChairResponses;
    }


    public List<Conference> showAllConferenceForPCMember(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long id=userRepository.findByUsername(username).getId();
        List<Conference> conferences = new ArrayList<>();
        List<PCMember> myRelated = pcMemberRepository.findAllByUserId(id);
        for (PCMember pcMember : myRelated) {
            Conference conference=conferenceRepository.findById(pcMember.getConferenceId()).orElse(null);
            if(conference==null){
                return null;
            }
            conferences.add(conference);
        }
        return conferences;
    }

    public List<Conference> showAllConferenceForAuthor(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        Long id=userRepository.findByUsername(username).getId();
        List<Conference> conferences = new ArrayList<>();
        List<Contributor> myRelated = authorRepository.findAllByUserId(id);
        for (Contributor contributor : myRelated) {
            Conference conference=conferenceRepository.findById(contributor.getConferenceId()).orElse(null);
            if(conference==null){
                return null;
            }
            conferences.add(conference);
        }
        return conferences;
    }

    public boolean openSubmission(String full_name){
        Conference conference = conferenceRepository.findByFullName(full_name);
        conference.setIsOpenSubmission(2);
        conferenceRepository.save(conference);
        return true;
    }

    public List<ShowSubmissionResponse> showSubmission(ShowSubmissionRequest request){
        User user=userRepository.findByUsername(request.getUsername());
        List<Article> articles=articleRepository.findByContributorID(user.getId());
        List<ShowSubmissionResponse> responses=new LinkedList<>();
        for(Article article:articles){
            Conference conference=conferenceRepository.findById(article.getConferenceID()).orElse(null);
            if(conference!=null){
                String name=conference.getFullName();
                responses.add(new ShowSubmissionResponse(name,article.getFilename(),article.getTitle(),article.getArticleAbstract(),article.getStatus()));
            }
        }
        return responses;
    }

    public List<Conference>  showAllConference(){
        List<Conference> conferences =  conferenceRepository.findAllByReviewStatus(2);
        return conferences;
    }

    private String getChairName(Long id){
        User user=userRepository.findById(id).orElse(null);
        if(user==null){
            return null;
        }
        return user.getUsername();

    }

    public List<AllConferenceResponse> getResponses2(List<ConferenceForChairResponse> conferences){
        List<AllConferenceResponse> responses=new LinkedList<>();
        for(ConferenceForChairResponse conferenceForChairResponse: conferences){
            Conference conference = conferenceForChairResponse.getConference();
            List<String> topicNames=new LinkedList<>();
            for(Topic topic:conference.getTopics()){
                topicNames.add(topic.getTopic());
            }
            String chairName=getChairName(conference.getChairId());
            AllConferenceResponse allConferenceResponse = new AllConferenceResponse(conference.getId(),conference.getFullName(),conference.getAbbreviation(),conference.getHoldingPlace(),conference.getHoldingTime(),conference.getSubmissionDeadline(),conference.getReviewReleaseDate(),conference.getReviewStatus(),chairName,conference.getIsOpenSubmission(),topicNames);
            allConferenceResponse.setCan_release(conferenceForChairResponse.getCanRelease());
            responses.add(allConferenceResponse);
        }
        return responses;
    }

    public List<AllConferenceResponse> getResponses(List<Conference> conferences){
        List<AllConferenceResponse> responses=new LinkedList<>();
        for(Conference conference: conferences){
            List<String> topicNames=new LinkedList<>();
            for(Topic topic:conference.getTopics()){
                topicNames.add(topic.getTopic());
            }
            String chairName=getChairName(conference.getChairId());
            responses.add(new AllConferenceResponse(conference.getId(),conference.getFullName(),conference.getAbbreviation(),conference.getHoldingPlace(),conference.getHoldingTime(),conference.getSubmissionDeadline(),conference.getReviewReleaseDate(),conference.getReviewStatus(),chairName,conference.getIsOpenSubmission(),topicNames));
        }
        return responses;
    }

    public String releaseReviewResult(long conference_id,long userId){
        Conference conference = conferenceRepository.findById(conference_id).orElse(null);
        if (conference.getChairId() == (userId)){
            conference.setReviewStatus(Math.max(conference.getReviewStatus(),4));
            return "开启成功";
        }
        else{
            return "开启失败";

        }
    }

    public String openManuscriptReview(OpenManuscriptReviewRequest request){
        Long conferenceId=request.getConference_id();
        if(conferenceId==null){
            return "请求错误";
        }
        Conference conference=conferenceRepository.findById(conferenceId).orElse(null);
        if(conference==null){
            return "服务器错误";
        }
        List<PCMember> pcMembersForConference=pcMemberRepository.findAllByConferenceId(conferenceId);
        if(pcMembersForConference==null){
            return "服务器错误";
        }

        if(pcMembersForConference.size()<2){
            return "PCMember数量少于2个，您不能开启投稿";
        }
        conference.setIsOpenSubmission(3);
        conferenceRepository.save(conference);

        List<Article> articles=articleRepository.findByConferenceID(conferenceId);
        if(request.getAllocationStrategy()==1){
            for(Article article:articles){
                allocateBasedOnTopics(article,pcMembersForConference);
            }
        }
        else{
            for(Article article:articles) {
                allocateAll(article, pcMembersForConference);
            }
        }
        return "开启投稿成功";
    }

    private void allocateBasedOnTopics(Article article,List<PCMember> pCMembers){
        List<PCMember> matchingPCMembers=new LinkedList<>();

        for(PCMember pcMember:pCMembers){
            if(isMatch(article,pcMember)){
                matchingPCMembers.add(pcMember);
            }
        }
        if(matchingPCMembers.size()<3){
            int[] random=getRandomNumbers(pCMembers.size(),3);
            saveAllocations(random,pCMembers,article);

        }
        else if(matchingPCMembers.size()==3){
            for(PCMember matchingPCMember:matchingPCMembers){
                saveAllocation(matchingPCMember,article);
            }
        }
        else{
            int[] random=getRandomNumbers(matchingPCMembers.size(),3);
            saveAllocations(random,matchingPCMembers,article);
        }

    }

    private void allocateAll(Article article,List<PCMember> pCMembers){
        List<PCMember> temp=new LinkedList<>(pCMembers);
        int minimumNumber=pCMembers.get(0).getArticles().size();

        for(int i=0;i<3;i++){
            for(PCMember pcMember:pCMembers){
                if(pcMember.getArticles().size()<minimumNumber){
                    minimumNumber=pcMember.getArticles().size();
                }
            }
            List<PCMember> feasiblePCMembers=new LinkedList<>();
            for(PCMember pcMember:temp){
                if(pcMember.getArticles().size()<=minimumNumber){
                    feasiblePCMembers.add(pcMember);
                }
            }
            int pcMemberSelectedIndex=(new Random().nextInt())%feasiblePCMembers.size();
            PCMember matchingPCMember=feasiblePCMembers.get(pcMemberSelectedIndex);
            saveAllocation(matchingPCMember,article);
            temp.remove(matchingPCMember);
        }
    }

    private boolean isMatch(Article article,PCMember pcMember){
        Set<Topic> topicsForArticles=article.getTopics();
        Set<Topic> topicsForPCMember=pcMember.getTopics();
        for(Topic topicForArticle:topicsForArticles){
            for(Topic topicForPCMember:topicsForPCMember){
                if(topicForArticle.getTopic().equals(topicForPCMember.getTopic())){
                    return true;
                }
            }
        }
        return false;
    }

    private void saveAllocation(PCMember pcMember,Article article){
        Set<Article> articles=pcMember.getArticles();
        Set<PCMember> articlePCMembers=article.getPcMembers();
        articlePCMembers.add(pcMember);
        articles.add(article);
        article.setPcMembers(articlePCMembers);
        pcMember.setArticles(articles);
        articleRepository.save(article);
    }

    private void saveAllocations(int[] random,List<PCMember> pcMembers,Article article){
        for(int i=0;i<random.length;i++){
            PCMember pcMember=pcMembers.get(random[i]);
            Set<Article> articles=pcMember.getArticles();
            Set<PCMember> articlePCMembers=article.getPcMembers();
            articles.add(article);
            articlePCMembers.add(pcMember);
            pcMember.setArticles(articles);
            article.setPcMembers(articlePCMembers);
            articleRepository.save(article);
        }
    }

    private static int[] getRandomNumbers(int max,int n){
        //初始化给定范围的待选数组
        int[] source = new int[max];
        for (int i = 0; i < source.length; i++){
            source[i] = i;
        }

        int[] result = new int[n];
        SecureRandom rd=new SecureRandom();
        int index ;
        int len=source.length;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }



}
