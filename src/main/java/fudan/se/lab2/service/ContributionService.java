package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.*;
import fudan.se.lab2.controller.request.componment.WriterRequest;
import fudan.se.lab2.controller.response.ArticleForPCMemberResponse;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: lab2
 * @description: 提供上传稿件、查看稿件的服务
 * @author: Shen Zhengyu
 * @create: 2020-04-08 09:20
 **/
@Service
public class ContributionService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PCMemberRepository pcMemberRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ResultRepository resultRepository;

    public String contribute(ContributionRequest contributionRequest){
       Article article = articleRepository.findByTitleAndConferenceID(contributionRequest.getTitle(),contributionRequest.getConferenceID());
        if (article!=null){
            return "duplicate contribution";
        }
        saveContribution(contributionRequest);
        return"successful contribution";
    }

    private String saveContribution(ContributionRequest contributionRequest){
        List<Writer> writers=new ArrayList<>();
        for(WriterRequest writerRequest:contributionRequest.getWriters()){
            writers.add(new Writer(writerRequest.getWriterName(),writerRequest.getEmail(),writerRequest.getInstitution(),writerRequest.getCountry()));
        }
        Article article = new Article(contributionRequest.getConferenceID(),contributionRequest.getContributorID(),contributionRequest.getFilename(),contributionRequest.getTitle(),contributionRequest.getArticleAbstract(),writers);
        Set<Topic> topics=new HashSet<>();
        for(String topicName:contributionRequest.getTopics()){
            Topic topic=topicRepository.findByTopic(topicName);
            if(topic==null){
                return "error";
            }
            Set<Article> articles=topic.getArticles();
            articles.add(article);
            topic.setArticles(articles);
            topics.add(topic);
        }
        article.setTopics(topics);
        articleRepository.save(article);

        Contributor contributor = new Contributor(contributionRequest.getContributorID(),contributionRequest.getConferenceID());
        authorRepository.save(contributor);
        return "successful contribution";
    }

    /**
    * @Description: 思路：查找一个PCMember所有的分配到的稿件
    * @Param: [reviewArticleRequest]
    * @return: java.util.HashMap<java.lang.String,java.lang.Object>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/6
    */

    public HashMap<String,Object> reviewArticle(ReviewArticleRequest reviewArticleRequest){
        PCMember pcMember = pcMemberRepository.findByUserIdAndConferenceId(reviewArticleRequest.getUserId(),reviewArticleRequest.getConference_id());
        HashMap<String, Object> hashMap = new HashMap<>();
        if (null != pcMember) {
            Set<Topic> topicSet = pcMember.getTopics();
            Set<Article> articleSet = pcMember.getArticles();

            List<Topic> topics = new ArrayList<>(topicSet);
            hashMap.put("message","请求成功");
            hashMap.put("topics",topics);
            Set<Article> articlesHaveReviewed = pcMember.getArticlesHaveReviewed();
            List<ArticleForPCMemberResponse> articleForPCMemberResponses = new ArrayList<>();
            for (Article article : articleSet) {
                ArticleForPCMemberResponse articleForPCMemberResponse = new ArticleForPCMemberResponse();
                articleForPCMemberResponse.setArticleAbstract(article.getArticleAbstract());
                articleForPCMemberResponse.setWriters(article.getWriters());
                for (Article article1 : articlesHaveReviewed) {
                    if (article1.getConferenceID().equals(reviewArticleRequest.getConference_id()) && article1.getTitle().equals(article.getTitle())){
                        //已经审稿
                        articleForPCMemberResponse.setStatus(1);
                    }else{
                        articleForPCMemberResponse.setStatus(0);
                    }
                }
                articleForPCMemberResponse.setTitle(article.getTitle());
                articleForPCMemberResponses.add(articleForPCMemberResponse);
            }
            hashMap.put("articles",articleForPCMemberResponses);
        }else{
            hashMap.put("message","请求失败");
        }
        return hashMap;
    }


    public HashMap<String,Object> showContributionModification(ShowContributionModificationRequest showContributionModificationRequest){
        List<Article> articles = articleRepository.findArticleByTitle(showContributionModificationRequest.getTitle());
        HashMap<String,Object> hashMap = new HashMap<>();
        for (Article article : articles) {
            if (article.getConferenceID().equals(showContributionModificationRequest.getConferenceID())){
                hashMap.put("message","success");
                hashMap.put("title",article.getTitle());
                hashMap.put("articleAbstract",article.getArticleAbstract());
                hashMap.put("fileName",article.getFilename());
                hashMap.put("authorID",article.getContributorID());
                hashMap.put("writers",article.getWriters());
                List<String> topicNames=new ArrayList<>();
                for(Topic topic:article.getTopics()){
                    topicNames.add(topic.getTopic());
                }
                hashMap.put("topics",topicNames);
                return hashMap;
            }
        }
        hashMap.put("message","预览失败");
        return hashMap;
    }

    /**
    * @Description: 修改投稿
    * @Param:
    * @return:
    * @Author: Shen Zhengyu
    * @Date: 2020/5/4
    */
    public HashMap<String,Object> modifyContribution(ModifyContributionRequest modifyContributionRequest) {
        HashMap<String,Object> hashMap = new HashMap<>();
        Article article = articleRepository.findByTitleAndConferenceID(modifyContributionRequest.getOriginalTitle(),modifyContributionRequest.getConferenceID());
        if (null == article){
             hashMap.put("message","没有该文章");
            return hashMap;
        }
        article.setArticleAbstract(modifyContributionRequest.getArticleAbstract());
        article.setTitle(modifyContributionRequest.getTitle());
        Set<Topic> topics = new HashSet<>();
        for (String topicName : modifyContributionRequest.getTopics()) {
            Topic topic=topicRepository.findByTopic(topicName);
            if(topic==null){
                hashMap.put("message","修改失败");
                return hashMap;
            }
            topics.add(topic);
        }
        article.setTopics(topics);

        article.setWriters(writerRequestToWriter(modifyContributionRequest.getWriters()));
        articleRepository.save(article);
        hashMap.put("message","修改成功");
        return hashMap;
    }


        /**
        * @Description: 添加新平均
        * @Param: [submitReviewResultRequest]
        * @return: java.lang.String
        * @Author: Shen Zhengyu
        * @Date: 2020/5/2
        */

    public String submitReviewResult(SubmitReviewResultRequest submitReviewResultRequest){
        //单人的评价
        Article article = articleRepository.findByTitleAndConferenceID(submitReviewResultRequest.getTitle(),submitReviewResultRequest.getConference_id());
        if (article == null){
            return "NOT FOUND";
        }
        Evaluation evaluation = new Evaluation(submitReviewResultRequest.getUserId(),submitReviewResultRequest.getScore(),submitReviewResultRequest.getComment(),submitReviewResultRequest.getConfidence());
        evaluationRepository.save(evaluation);


        //总评价
        Result result = resultRepository.findResultByConferenceIDAndTitle(submitReviewResultRequest.getConference_id(),submitReviewResultRequest.getTitle());
        if(null == result){
            Set<Evaluation> evaluations = new HashSet<>();
            evaluations.add(evaluation);
            resultRepository.save(new Result(submitReviewResultRequest.getConference_id(),submitReviewResultRequest.getTitle(),evaluations));
        }else{
            Set<Evaluation> evaluations = result.getEvaluations();
            evaluations.add(evaluation);
            resultRepository.save(result);
        }
        PCMember pcMember = pcMemberRepository.findByUserIdAndConferenceId(submitReviewResultRequest.getUserId(),submitReviewResultRequest.getConference_id());
        Set<Article> articles = pcMember.getArticlesHaveReviewed();
        articles.add(article);
        pcMember.setArticlesHaveReviewed(articles);
        pcMemberRepository.save(pcMember);

        article.setHowManyPeopleHaveReviewed(article.getHowManyPeopleHaveReviewed()+1);
        if (article.getHowManyPeopleHaveReviewed() == 3){
            article.setStatus((long)1);
        }
        articleRepository.save(article);
        return"successful contribution";
    }

    private ArrayList<Writer> writerRequestToWriter(List<WriterRequest> writerRequests){
        ArrayList<Writer> writers = new ArrayList<>();
        for (WriterRequest writerRequest : writerRequests) {

            Writer writer = new Writer(writerRequest.getWriterName(),writerRequest.getEmail(),writerRequest.getInstitution(),writerRequest.getCountry());
        writers.add(writer);
        }
        return writers;
    }

}
