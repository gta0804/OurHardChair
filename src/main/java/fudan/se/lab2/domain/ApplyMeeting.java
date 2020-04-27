package fudan.se.lab2.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ApplyMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long applicantId;
    private Long verifierId;

    private String abbreviation;
    @Column(unique = true)
    private String fullName;
    private String holdingTime;
    private String holdingPlace;
    private String submissionDeadline;
    private String reviewReleaseDate;
    //1审核中，2审核通过，3审核未通过
    private Integer reviewStatus;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Topic> topics;


    public ApplyMeeting(){}

    public ApplyMeeting(Long applicantId,Long verifierId, String abbreviation, String fullName, String holdingTime, String holdingPlace, String submissionDeadline, String reviewReleaseDate,Integer reviewStatus,List<Topic> topics){
        this.applicantId=applicantId;
        this.verifierId=verifierId;
        this.abbreviation=abbreviation;
        this.fullName=fullName;
        this.holdingTime=holdingTime;
        this.holdingPlace=holdingPlace;
        this.submissionDeadline=submissionDeadline;
        this.reviewReleaseDate=reviewReleaseDate;
        this.reviewStatus=reviewStatus;
        this.topics = topics;
    }

    public Long getVerifierId() {
        return verifierId;
    }

    public void setVerifierId(Long verifierId) {
        this.verifierId = verifierId;
    }

    public Long getApplicantId(){
        return applicantId;
    }

    public void setApplicantId(Long applicantId){
        this.applicantId=applicantId;
    }


    public String getAbbreviation(){
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation){
        this.abbreviation=abbreviation;
    }

    public String getFullName(){
        return fullName;
    }

    public void setFullName(String fullName){
        this.fullName=fullName;
    }

    public String getHoldingTime(){
        return holdingTime;
    }

    public void setHoldingTime(String holdingTime){
        this.holdingTime=holdingTime;
    }

    public String getHoldingPlace(){
        return holdingPlace;
    }

    public void setHoldingPlace(String holdingPlace){
        this.holdingPlace=holdingPlace;
    }

    public String getSubmissionDeadline(){
        return  submissionDeadline;
    }

    public void setSubmissionDeadline(String submissionDeadline){
        this.submissionDeadline=submissionDeadline;
    }

    public String getReviewReleaseDate(){
        return  reviewReleaseDate;
    }

    public void setReviewReleaseDate(String reviewReleaseDate){
        this.reviewReleaseDate=reviewReleaseDate;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public ArrayList<Topic> getTopics() {

        return (ArrayList<Topic>)topics;
    }

    public void setTopics(ArrayList<Topic> topics) {
        this.topics = topics;
    }
}