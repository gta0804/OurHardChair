package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.OpenManuscriptReviewRequest;
import fudan.se.lab2.controller.request.ShowSubmissionRequest;
import fudan.se.lab2.controller.response.ShowSubmissionResponse;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public List<Conference> showAllConferenceForChair(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long id = userRepository.findByUsername(username).getId();
        List<Conference> conferences = conferenceRepository.findAllByChairId(id);
        return conferences;
    }

    public List<Conference> showAllConference(){
        List<Conference> conferences = (List<Conference>) conferenceRepository.findAll();
        return conferences;
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

    public String getChairName(Long id){
        User user=userRepository.findById(id).orElse(null);
        if(user==null){
            return null;
        }
        return user.getUsername();

    }

    public String openManuscriptReview(OpenManuscriptReviewRequest request){
        Long conferenceId=request.getConferenceId();
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
                Set<Article> articles=matchingPCMember.getArticles();
                articles.add(article);
                matchingPCMember.setArticles(articles);
                articleRepository.save(article);
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
            int pcMemberSelectedIndex=new Random().nextInt()%feasiblePCMembers.size();
            PCMember pcMemberSelected=feasiblePCMembers.get(pcMemberSelectedIndex);
            Set<Article> articles=pcMemberSelected.getArticles();
            articles.add(article);
            pcMemberRepository.save(pcMemberSelected);
            temp.remove(pcMemberSelected);
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

    private void saveAllocations(int[] random,List<PCMember> pcMembers,Article article){
        for(int i=0;i<random.length;i++){
            Set<Article> articles=pcMembers.get(random[i]).getArticles();
            articles.add(article);
            pcMembers.get(random[i]).setArticles(articles);
            articleRepository.save(article);
        }
    }

    private static int[] getRandomNumbers(int max,int n){
        //初始化给定范围的待选数组
        int[] source = new int[max+1];
        for (int i = 0; i < source.length; i++){
            source[i] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
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
