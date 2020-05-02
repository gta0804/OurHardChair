package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-02 15:47
 **/
@Controller
public class SubmitReviewResultRequest {
    private Long conferenceID;
    private Long userId;
    private String title;
    private Integer score;
    private String comment;
    private Integer confidence;

    @Autowired
    public SubmitReviewResultRequest(){}
    public SubmitReviewResultRequest(Long conferenceID, Long userId, String title, Integer score, String comment, Integer confidence) {
        this.conferenceID = conferenceID;
        this.userId = userId;
        this.title = title;
        this.score = score;
        this.comment = comment;
        this.confidence = confidence;
    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }
}
