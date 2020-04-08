package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.ContributionRequest;
import fudan.se.lab2.domain.ApplyMeeting;
import fudan.se.lab2.domain.Article;
import fudan.se.lab2.domain.Author;
import fudan.se.lab2.repository.ArticleRepository;
import fudan.se.lab2.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: lab2
 * @description: 提供上传稿件的服务
 * @author: Shen Zhengyu
 * @create: 2020-04-08 09:20
 **/
@Service
public class ContributionService {
    @Autowired
    private ArticleRepository articleRepository;

    private AuthorRepository authorRepository;

    public String saveContribution(ContributionRequest contributionRequest){
        Article article = articleRepository.findArticleByTitle(contributionRequest.getTitle());
        if (article != null) {
            return "duplicate contribution";
        } else {
            Article article1 = new Article(contributionRequest.getConferenceID(),contributionRequest.getAuthorID(),contributionRequest.getFilename(),contributionRequest.getTitle(),contributionRequest.getArticleAbstract());
            articleRepository.save(article1);
            Author author = new Author(contributionRequest.getAuthorID(),contributionRequest.getConferenceID());
            authorRepository.save(author);
            return "successful contribution";
        }

    }

}
