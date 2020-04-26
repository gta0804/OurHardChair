package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.ContributionRequest;
import fudan.se.lab2.controller.request.ReviewArticleRequest;
import fudan.se.lab2.domain.ApplyMeeting;
import fudan.se.lab2.domain.Article;
import fudan.se.lab2.domain.Author;
import fudan.se.lab2.repository.ArticleRepository;
import fudan.se.lab2.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

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

    public String saveContribution(ContributionRequest contributionRequest){
        List<Article> articles = articleRepository.findArticleByTitle(contributionRequest.getTitle());
        for (Article article : articles) {
            if (article.getConferenceID().equals(contributionRequest.getConferenceID())){
                return "duplicate contribution";
            } else {
                System.out.println(contributionRequest.getAuthorID());
                System.out.println(contributionRequest.getConferenceID());
                System.out.println(contributionRequest.getTitle());
                System.out.println(contributionRequest.getArticleAbstract());
                System.out.println(contributionRequest.getFilename());
                System.out.println("contributionRequest.getFilename()");

                Article article1 = new Article(contributionRequest.getConferenceID(),contributionRequest.getAuthorID(),contributionRequest.getFilename(),contributionRequest.getTitle(),contributionRequest.getArticleAbstract());
                articleRepository.save(article1);
                System.out.println(contributionRequest.getAuthorID());
                System.out.println(contributionRequest.getConferenceID());

                Author author = new Author(contributionRequest.getAuthorID(),contributionRequest.getConferenceID());
                authorRepository.save(author);
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
                hashMap.put("message","预览成功");
                hashMap.put("title",article.getTitle());
                hashMap.put("articleAbstract",article.getArticleAbstract());
                hashMap.put("articlePath","/workplace/upload/"+article.getFilename());
                hashMap.put("status",article.getStatus().toString());
                return hashMap;
            }
        }
        hashMap.put("message","预览失败，没有找到改文件");
        return hashMap;
    }
}
