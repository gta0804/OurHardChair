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
    private ConferenceRepository conferenceRepository;

    private PCMemberRepository pcMemberRepository;

    private AuthorRepository authorRepository;

    private ArticleRepository articleRepository;

    private UserRepository userRepository;

    private EvaluationModifyRequestRepository evaluationModifyRequestRepository;

    private ResultRepository resultRepository;

    private EvaluationRepository evaluationRepository;
    @Autowired
    public MyRelatedConferenceService(ConferenceRepository conferenceRepository,PCMemberRepository pcMemberRepository,AuthorRepository authorRepository,ArticleRepository articleRepository,UserRepository userRepository){
        this.conferenceRepository=conferenceRepository;
        this.pcMemberRepository=pcMemberRepository;
        this.authorRepository=authorRepository;
        this.articleRepository=articleRepository;
        this.userRepository=userRepository;
    }

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
            Conference conference = conferenceRepository.findById(pcMember.getConferenceId()).orElse(null);
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
        Set<Long> conferenceIDList=new HashSet<>();
        for(Contributor contributor : myRelated){
            conferenceIDList.add(contributor.getConferenceId());
        }
        for (Long conferenceId: conferenceIDList) {
            Conference conference=conferenceRepository.findById(conferenceId).orElse(null);
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
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(username);
        List<Article> articles=articleRepository.findByContributorIDAndConferenceID(user.getId(),request.getConference_id());
        List<ShowSubmissionResponse> responses=new LinkedList<>();
        for(Article article:articles){
            Conference conference=conferenceRepository.findById(article.getConferenceID()).orElse(null);
            if(conference!=null){
                String name=conference.getFullName();
                ShowSubmissionResponse showSubmissionResponse = new ShowSubmissionResponse(name,article.getFilename(),article.getTitle(),article.getArticleAbstract(),article.getStatus());
                Set<Topic> topicsRaw = article.getTopics();
                Set<String> topics = new HashSet<>();

                for (Topic topic : topicsRaw) {
                    topics.add(topic.getTopic());
                }
                showSubmissionResponse.setTopics(topics);
                showSubmissionResponse.setWriters(article.getWriters());
                showSubmissionResponse.setArticleID(article.getId());
                responses.add(showSubmissionResponse);
            }
        }
        return responses;
    }

    public List<Conference>  showAllConference(){
        return conferenceRepository.findAllByReviewStatus(2);
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

    public String releaseReviewResult(Long conference_id){
        Conference conference = conferenceRepository.findById(conference_id).orElse(null);

        if(null != conference) {
            List<Article> articles = articleRepository.findByConferenceID(conference.getId());
            for (Article article : articles) {
                if (!article.getHowManyPeopleHaveReviewed().equals(3)){
                    return "开启失败，有稿件未审完";
                }
            }
            for(Article article:articles){
                article.setStatus((long)2);
                articleRepository.save(article);
            }
            conference.setIsOpenSubmission(Math.max(conference.getIsOpenSubmission(), 4));
            conferenceRepository.save(conference);
            return "开启成功";
        }else {
            return "开启失败";
        }
    }

    public String releaseReviewResultAgain(Long conference_id){
        Conference conference = conferenceRepository.findById(conference_id).orElse(null);

        if(null != conference) {
            List<Article> articles = articleRepository.findByConferenceID(conference.getId());
            for (Article article : articles) {
                if (!article.getHowManyPeopleHaveReviewed().equals(3)){
                    return "开启失败，有稿件未审完";
                }
            }
            for(Article article:articles){
                article.setStatus((long)2);
                articleRepository.save(article);
            }
            conference.setIsOpenSubmission(Math.max(conference.getIsOpenSubmission(), 4));
            conferenceRepository.save(conference);
            updateEvaluation(conference_id);
            return "开启成功";
        }else {
            return "开启失败";
        }
    }
    /**
    * @Description: 更新一个会议的每篇文章的审阅结果
    * @Param: [conference_id]
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/5/29
    */
    private void updateEvaluation(Long conference_id){
        HashSet<EvaluationModifyRequest> evaluationModifyRequests = (HashSet<EvaluationModifyRequest>) evaluationModifyRequestRepository.findAllByConferenceID(conference_id);
        for (EvaluationModifyRequest evaluationModifyRequest : evaluationModifyRequests) {
            Result result = resultRepository.findByArticleIDAndConferenceID(evaluationModifyRequest.getArticleID(),evaluationModifyRequest.getConferenceID());
            HashSet<Evaluation> evaluationSet = (HashSet<Evaluation>) result.getEvaluations();
            Evaluation evaluationToModify = null;
            for (Evaluation evaluation : evaluationSet) {
                if (evaluation.getPCMemberID().equals(evaluationModifyRequest.getPCMemberID())){
                    evaluationToModify = evaluation;
                    break;
                }
            }
            if(null != evaluationToModify) {
                evaluationToModify.setComment(evaluationModifyRequest.getComment());
                evaluationToModify.setConfidence(evaluationModifyRequest.getConfidence());
                evaluationToModify.setScore(evaluationModifyRequest.getScore());
                evaluationRepository.save(evaluationToModify);
                resultRepository.save(result);
                evaluationModifyRequestRepository.delete(evaluationModifyRequest);
            }
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

        if(pcMembersForConference.size()<3){
            return "邀请的PCMember数量少于2个，您不能开启审稿";
        }
        if(conference.getIsOpenSubmission()!=2){
            return "会议状态不符";
        }

        List<Article> articles=articleRepository.findByConferenceID(conferenceId);
        HashMap<Article,List<PCMember>> results=new HashMap<>();
        if(request.getAllocationStrategy()==1){
            for(Article article:articles){
               if(!allocateBasedOnTopics(article,pcMembersForConference,results)){
                   return"邀请的PCMember不符合条件导致无法分配";
               }
            }
        }
        else{
            for(Article article:articles) {
               if(!allocateAll(article, request,results)){
                   return"邀请的PCMember不符合条件导致无法分配";
               }
            }
        }
        save(results,articles);
        conference.setIsOpenSubmission(3);
        conferenceRepository.save(conference);
        return "开启投稿成功";
    }

    private boolean allocateBasedOnTopics(Article article,List<PCMember> pCMembers,HashMap<Article,List<PCMember>> results){
        List<PCMember> matchingPCMembers=new LinkedList<>();
        Iterator<PCMember> pcMemberIterator=pCMembers.iterator();
        while(pcMemberIterator.hasNext()){
            PCMember temp=pcMemberIterator.next();
            if(isNotFit(article,temp)){
                pcMemberIterator.remove();
            }
        }
        if(pCMembers.size()<3){
            return false;
        }

        for(PCMember pcMember:pCMembers){
            if(isMatch(article,pcMember)){
                matchingPCMembers.add(pcMember);
            }
        }

        if(matchingPCMembers.size()<3){
            int[] random=getRandomNumbers(pCMembers.size());
            for(int index:random){
                saveAllocation(pCMembers.get(index),article,results);
            }
        }
        else if(matchingPCMembers.size()==3){
            for(PCMember matchingPCMember:matchingPCMembers){
                saveAllocation(matchingPCMember,article,results);
            }
        }
        else{
            int[] random=getRandomNumbers(matchingPCMembers.size());
            for(int index:random){
                saveAllocation(matchingPCMembers.get(index),article,results);
            }
        }
        return true;

    }

    private boolean allocateAll(Article article,OpenManuscriptReviewRequest request,HashMap<Article,List<PCMember>> results) {
        List<PCMember> pCMembers = pcMemberRepository.findAllByConferenceId(request.getConference_id());
        List<PCMember> temp = new LinkedList<>(pCMembers);
        int minimumNumber = 0;

        for (int i = 0; i < 3; i++) {
            for (PCMember pcMember : pCMembers) {
                minimumNumber = pCMembers.get(0).getArticles().size();
                if (pcMember.getArticles().size() < minimumNumber) {
                    minimumNumber = pcMember.getArticles().size();
                }
            }
            List<PCMember> feasiblePCMembers = new LinkedList<>();
            for (PCMember pcMember : temp) {
                if (pcMember.getArticles().size() <= minimumNumber) {
                    feasiblePCMembers.add(pcMember);
                }
            }

            for(Iterator<PCMember> pcMemberIterator=feasiblePCMembers.iterator();pcMemberIterator.hasNext();){
                PCMember feasiblePCMember=pcMemberIterator.next();
                if(isNotFit(article,feasiblePCMember)){
                    pcMemberIterator.remove();
                }
            }
            if(feasiblePCMembers.size()<1){
                return false;
            }
            int pcMemberSelectedIndex = (new Random().nextInt(Integer.MAX_VALUE)) % feasiblePCMembers.size();
            PCMember matchingPCMember = feasiblePCMembers.get(pcMemberSelectedIndex);
            saveAllocation(matchingPCMember, article,results);
            temp.remove(matchingPCMember);
        }
        return true;
    }

    /*
    判断稿件topic与PCMEMBER负责topic是否相符
     */
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
    /*
    判断稿件是否可以被该PCMember审阅
     */

    private boolean isNotFit(Article article, PCMember pcMember){
        if(article.getContributorID().equals(pcMember.getUserId())){
            return true;
        }
        User user=userRepository.findById(pcMember.getUserId()).orElse(null);
        if(user==null){
            return true;
        }
        List<Writer> writers=article.getWriters();
        for(Writer writer:writers){
            if(writer.getWriterName().equals(user.getFullName())&&writer.getEmail().equals(user.getEmail())){
                return true;
            }
        }
        return false;
    }

    private void saveAllocation(PCMember pcMember,Article article,HashMap<Article,List<PCMember>> results){
        List<PCMember> pcMembers=results.containsKey(article)?results.get(article):new LinkedList<>();
        pcMembers.add(pcMember);
        results.put(article,pcMembers);
    }

    private void save(HashMap<Article,List<PCMember>> results,List<Article> articles){
        for(Article article:articles){
            List<PCMember> pcMembers=results.get(article);
            if(pcMembers!=null){
                for(PCMember pcMember:pcMembers){
                    Set<Article> articleForPCMembers=pcMember.getArticles();
                    articleForPCMembers.add(article);
                    pcMember.setArticles(articleForPCMembers);
                }
                pcMemberRepository.saveAll(pcMembers);
            }
        }
    }



    private static int[] getRandomNumbers(int max){
        //初始化给定范围的待选数组
        int[] source = new int[max];
        for (int i = 0; i < source.length; i++){
            source[i] = i;
        }

        int[] result = new int[3];
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
