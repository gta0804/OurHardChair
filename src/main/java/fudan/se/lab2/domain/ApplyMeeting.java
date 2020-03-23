package fudan.se.lab2.domain;

import javax.persistence.*;
@Entity
public class ApplyMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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



}