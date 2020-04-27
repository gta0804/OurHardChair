package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.ContributionRequest;
import fudan.se.lab2.controller.request.ReviewArticleRequest;
import fudan.se.lab2.controller.request.ShowContributionModificationRequest;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.repository.ArticleRepository;
import fudan.se.lab2.repository.AuthorRepository;
import fudan.se.lab2.repository.ConferenceRepository;
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
    private ConferenceRepository conferenceRepository;

    public String saveContribution(ContributionRequest contributionRequest){
        List<Article> articles = articleRepository.findArticleByTitle(contributionRequest.getTitle());
        for (Article article : articles) {
            if (article.getConferenceID().equals(contributionRequest.getConferenceID())){
                return "duplicate contribution";
            } else {
                System.out.println(contributionRequest.getContributorID());
                System.out.println(contributionRequest.getConferenceID());
                System.out.println(contributionRequest.getTitle());
                System.out.println(contributionRequest.getArticleAbstract());
                System.out.println(contributionRequest.getFilename());
                System.out.println("contributionRequest.getFilename()");
                Article article1 = new Article(contributionRequest.getConferenceID(),contributionRequest.getContributorID(),contributionRequest.getFilename(),contributionRequest.getTitle(),contributionRequest.getArticleAbstract(),contributionRequest.getWriters());
                articleRepository.save(article1);
                System.out.println(contributionRequest.getContributorID());
                System.out.println(contributionRequest.getConferenceID());

                Contributor contributor = new Contributor(contributionRequest.getContributorID(),contributionRequest.getConferenceID());
                authorRepository.save(contributor);
                return "successful contribution";
            }
        }
        return "unsuccessful contribution";
    }

    public HashMap<String,Object> reviewArticle(ReviewArticleRequest reviewArticleRequest){
        List<Article> articles = articleRepository.findArticleByTitle(reviewArticleRequest.getTitle());
        HashMap<String,Object> hashMap = new HashMap<>();
        for (Article article : articles) {
            if (article.getConferenceID().equals(reviewArticleRequest.getConferenceID())){
                String conferenceFullName = (conferenceRepository.findById(article.getConferenceID())).get().getFullName();
                ArrayList<Topic> topics = conferenceRepository.findByFullName(conferenceFullName).getTopics();
                ArrayList<String> topicStrings = new ArrayList<>();
                for (Topic topic : topics) {
                    topicStrings.add(topic.getTopic());
                }
                hashMap.put("message","预览成功");
                hashMap.put("title",article.getTitle());
                hashMap.put("articleAbstract",article.getArticleAbstract());
                hashMap.put("articlePath","/workplace/upload/"+article.getFilename());
                hashMap.put("status",article.getStatus().toString());
                hashMap.put("topics",topicStrings);
                return hashMap;
            }
        }
        hashMap.put("message","预览失败，没有找到改文件");
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
                return hashMap;
            }
        }
        hashMap.put("message","预览失败");
        return hashMap;
    }

}
