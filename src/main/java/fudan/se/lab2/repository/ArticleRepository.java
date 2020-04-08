package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Article;
import fudan.se.lab2.domain.Conference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article,Long> {
    Article findArticleByTitle(String title);
}
