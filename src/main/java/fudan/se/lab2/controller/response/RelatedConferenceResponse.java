package fudan.se.lab2.controller.response;

import fudan.se.lab2.domain.Topic;

import java.util.*;

public  class RelatedConferenceResponse {
    private String full_name;
    private String short_name;
    private String place;
    private String start_date;
    private String deadline_date;
    private String release_date;
    private Integer reviewStatus;
    private String chair_name;
    private Integer status;
    private Set<Topic> topics;
    public RelatedConferenceResponse(String full_name, String short_name, String place, String start_date, String deadline_date, String release_date, Integer reviewStatus, String chair_name, Integer status,Set<Topic> topics){
        this.full_name = full_name;
        this.short_name = short_name;
        this.place = place;
        this.start_date = start_date;
        this.deadline_date = deadline_date;
        this.release_date = release_date;
        this.reviewStatus = reviewStatus;
        this.chair_name = chair_name;
        this.status = status;
        this.topics=topics;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getDeadline_date() {
        return deadline_date;
    }

    public void setDeadline_date(String deadline_date) {
        this.deadline_date = deadline_date;
    }

    public String getChair_name() {
        return chair_name;
    }

    public void setChair_name(String chair_name) {
        this.chair_name = chair_name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }
}
