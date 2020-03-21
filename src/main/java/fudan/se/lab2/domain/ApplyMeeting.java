package fudan.se.lab2.domain;

import javax.persistence.*;
@Entity
public class ApplyMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long applicantId;

    private String abbreviation;
    @Column(unique = true)
    private String fullName;
    private String holdingTime;
    private String holdingPlace;
    private String submissionDeadline;
    private String reviewReleaseDate;

    public ApplyMeeting(){}

    public ApplyMeeting(Long applicantId, String abbreviation, String fullName, String holdingTime, String holdingPlace, String submissionDeadline, String reviewReleaseDate){
        this.applicantId=applicantId;
        this.abbreviation=abbreviation;
        this.fullName=fullName;
        this.holdingTime=holdingTime;
        this.holdingPlace=holdingPlace;
        this.submissionDeadline=submissionDeadline;
        this.reviewReleaseDate=reviewReleaseDate;
    }

    public Long getApplicantId(){
        return this.applicantId;
    }

    public void setApplicantId(Long applicantId){
        this.applicantId=applicantId;
    }

    public String getAbbreviation(){
        return this.abbreviation;
    }

    public void setAbbreviation(String abbreviation){
        this.abbreviation=abbreviation;
    }

    public String getFullName(){
        return this.fullName;
    }

    public void setFullName(String fullName){
        this.fullName=fullName;
    }

    public String getHoldingTime(){
        return this.holdingTime;
    }

    public void setHoldingTime(String holdingTime){
        this.holdingTime=holdingTime;
    }

    public String getHoldingPlace(){
        return this.holdingPlace;
    }

    public void setHoldingPlace(String holdingPlace){
        this.holdingPlace=holdingPlace;
    }

    public String getSubmissionDeadline(){
        return this.submissionDeadline;
    }

    public void setSubmissionDeadline(String submissionDeadline){
        this.submissionDeadline=submissionDeadline;
    }

    public String getReviewReleaseDate(){
        return this.reviewReleaseDate;
    }

    public void setReviewReleaseDate(String reviewReleaseDate){
        this.reviewReleaseDate=reviewReleaseDate;
    }



}