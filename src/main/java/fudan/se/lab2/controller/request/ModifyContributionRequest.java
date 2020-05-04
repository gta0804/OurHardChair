package fudan.se.lab2.controller.request;

import fudan.se.lab2.controller.request.componment.WriterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-04 22:48
 **/
@Controller
public class ModifyContributionRequest {
    private String originalTitle;

    private Long conferenceID;


    private String title;

    private String articleAbstract;


    private List<WriterRequest> writers;

    private Set<String> topics;

    @Autowired
    public ModifyContributionRequest() {

    }

    public ModifyContributionRequest(String originalTitle, Long conferenceID, String title, String articleAbstract, List<WriterRequest> writers, Set<String> topics) {
        this.originalTitle = originalTitle;
        this.conferenceID = conferenceID;
        this.title = title;
        this.articleAbstract = articleAbstract;
        this.writers = writers;
        this.topics = topics;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
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

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }
}
