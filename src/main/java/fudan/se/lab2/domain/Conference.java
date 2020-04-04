package fudan.se.lab2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long chairId;

    private String abbreviation;
    private String fullName;
    private String holdingPlace;
    private String holdingTime;
    private String submissionDeadline;
    private String reviewReleaseDate;
    private Integer isOpenSubmission;
    public Conference(){}

    public Conference(Long chairId,String abbreviation,String fullName,String holdingPlace,String holdingTime,String submissionDeadline,String reviewReleaseDate,Integer isOpenSubmission ){
        this.chairId=chairId;
        this.abbreviation=abbreviation;
        this.fullName=fullName;
        this.holdingPlace=holdingPlace;
        this.holdingTime=holdingTime;
        this.submissionDeadline=submissionDeadline;
        this.reviewReleaseDate=reviewReleaseDate;
        this.isOpenSubmission=isOpenSubmission;
    }

    public Long getChairId() {
        return chairId;
    }

    public void setChairId(Long chairId) {
        this.chairId = chairId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHoldingPlace() {
        return holdingPlace;
    }

    public void setHoldingPlace(String holdingPlace) {
        this.holdingPlace = holdingPlace;
    }

    public String getHoldingTime() {
        return holdingTime;
    }

    public void setHoldingTime(String holdingTime) {
        this.holdingTime = holdingTime;
    }

    public String getSubmissionDeadline() {
        return submissionDeadline;
    }

    public void setSubmissionDeadline(String submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }

    public String getReviewReleaseDate() {
        return reviewReleaseDate;
    }

    public void setReviewReleaseDate(String reviewReleaseDate) {
        this.reviewReleaseDate = reviewReleaseDate;
    }

    public Integer getIsOpenSubmission() {
        return isOpenSubmission;
    }

    public void setIsOpenSubmission(Integer isOpenSubmission) {
        this.isOpenSubmission = isOpenSubmission;
    }
}
