package fudan.se.lab2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: lab2
 * @description: ConferenceArticle
 * @author: Shen Zhengyu
 * @create: 2020-04-08 08:57
 **/
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long conferenceID;

    private Long AuthorID;

    private String filename;

    private String title;

    private String articleAbstract;

    //0未通过 1待审核 2已通过
    private Integer status;

    public Article() {
    }

    /**
    * @Description:
    * @Param: [conferenceID, AuthorID, filename, title, articleAbstract]
    * @return:
    * @Author: Shen Zhengyu
    * @Date: 2020/4/8
    */
    public Article(Long conferenceID,Long AuthorID,String filename,String title,String articleAbstract)
    {
        this.conferenceID = conferenceID;
        this.AuthorID = AuthorID;
        this.filename = filename;
        this.title = title;
        this.articleAbstract = articleAbstract;
        this.status = 1;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
    }

    public Long getAuthorID() {
        return AuthorID;
    }

    public void setAuthorID(Long authorID) {
        AuthorID = authorID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }
}
