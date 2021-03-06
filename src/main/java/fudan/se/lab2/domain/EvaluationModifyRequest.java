package fudan.se.lab2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-29 21:48
 **/
@Entity
public class EvaluationModifyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long PCMemberID;
    private Integer score;
    private String comment;
    private Integer confidence;
    private Long conferenceID;
    private Long articleID;
    public EvaluationModifyRequest(){}
    public EvaluationModifyRequest(Evaluation evaluation) {
        this.PCMemberID = evaluation.getPCMemberID();
        this.score = evaluation.getScore();
        this.comment = evaluation.getComment();
        this.confidence = evaluation.getConfidence();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPCMemberID() {
        return PCMemberID;
    }

    public void setPCMemberID(Long PCMemberID) {
        this.PCMemberID = PCMemberID;
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

    public Long getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
    }

    public Long getArticleID() {
        return articleID;
    }

    public void setArticleID(Long articleID) {
        this.articleID = articleID;
    }
}
