package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: lab2
 * @description: 投稿请求
 * @author: Shen Zhengyu
 * @create: 2020-04-08 09:11
 **/
@Controller
public class ContributionRequest {
    private Long conferenceID;

    private Long authorID;

    private String filename;

    private String title;

    private String articleAbstract;
    @Autowired
    public ContributionRequest(){

    }

    public ContributionRequest(Long conferenceID,Long authorID,String filename,String title,String articleAbstract){
        this.conferenceID = conferenceID;
        this.authorID = authorID;
        this.filename = filename;
        this.title = title;
        this.articleAbstract = articleAbstract;
    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
    }

    public Long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Long authorID) {
        this.authorID = authorID;
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
