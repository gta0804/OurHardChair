package fudan.se.lab2.controller.request;

import fudan.se.lab2.controller.request.componment.WriterRequest;
import fudan.se.lab2.domain.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * @program: lab2
 * @description: 投稿请求
 * @author: Shen Zhengyu
 * @create: 2020-04-08 09:11
 **/
@Controller
public class ContributionRequest {
    private Long conferenceID;

    private Long contributorID;

    private String filename;

    private String title;

    private String articleAbstract;


    private List<WriterRequest> writers;
    @Autowired
    public ContributionRequest(){

    }

    public ContributionRequest(Long conferenceID,Long authorID,String filename,String title,String articleAbstract,List<WriterRequest> writers){
        this.conferenceID = conferenceID;
        this.contributorID = authorID;
        this.filename = filename;
        this.title = title;
        this.articleAbstract = articleAbstract;
        this.writers = writers;
    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
    }

    public Long getContributorID() {
        return contributorID;
    }

    public void setContributorID(Long contributorID) {
        this.contributorID = contributorID;
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

    public List<WriterRequest> getWriters() {
        return writers;
    }

    public void setWriters(List<WriterRequest> writers) {
        this.writers = writers;
    }
}

