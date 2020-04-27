package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class ApplyMeetingRequest {
    //This variable holds the conference name for short
    private String abbreviation;
    //This variable holds the full name of the conference
    private String fullName;
    private String holdingTime;
    private String holdingPlace;
    private String submissionDeadline;
    private String reviewReleaseDate;
    private ArrayList<String> topics;
    @Autowired
    public ApplyMeetingRequest(){

    }

    public ApplyMeetingRequest(String abbreviation,String fullName, String holdingTime,String holdingPlace,String submissionDeadline,String reviewReleaseDate,ArrayList<String> topics){
        this.abbreviation=abbreviation;
        this.fullName=fullName;
        this.holdingTime=holdingTime;
        this.holdingPlace=holdingPlace;
        this.submissionDeadline=submissionDeadline;
        this.reviewReleaseDate=reviewReleaseDate;
        this.topics = topics;
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

    public ArrayList<String> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }
}
