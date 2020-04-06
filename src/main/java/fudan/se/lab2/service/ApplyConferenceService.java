package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.ApplyMeetingRequest;
import fudan.se.lab2.controller.request.ApproveConferenceRequest;
import fudan.se.lab2.controller.request.DisproveConferenceRequest;
import fudan.se.lab2.domain.ApplyMeeting;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.ApplyMeetingRepository;
import fudan.se.lab2.repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ApplyConferenceService {
    @Autowired
    private ApplyMeetingRepository applyMeetingRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    public  ApplyConferenceService(ApplyMeetingRepository applyMeetingRepository,ConferenceRepository conferenceRepository){
        this.applyMeetingRepository=applyMeetingRepository;
        this.conferenceRepository=conferenceRepository;
    }

    public ApplyMeeting applyMeeting(ApplyMeetingRequest request, Long id){
        if(null!=applyMeetingRepository.findByFullName(request.getFullName())){
            System.out.println("会议全称重复");
            return null;
        }
        else{
            ApplyMeeting applyMeeting=new ApplyMeeting(id,new Long(1),request.getAbbreviation(),request.getAbbreviation(),request.getHoldingTime(),request.getHoldingPlace(),request.getSubmissionDeadline(),request.getReviewReleaseDate(),1);
            applyMeetingRepository.save(applyMeeting);
            return applyMeeting;
        }
    }

    public Conference approveConference(ApproveConferenceRequest request) {
        System.out.println(request.getFullName());
        ApplyMeeting applyMeeting = applyMeetingRepository.findByFullName(request.getFullName());
        if (applyMeeting == null) {
            //会议申请表中没有此会议
            return null;
        } else {
            Conference conference = new Conference(applyMeeting.getApplicantId(), applyMeeting.getAbbreviation(), applyMeeting.getFullName(), applyMeeting.getHoldingPlace(), applyMeeting.getHoldingTime(), applyMeeting.getSubmissionDeadline(), applyMeeting.getReviewReleaseDate(), 1);
            conferenceRepository.save(conference);
            applyMeeting.setReviewStatus(2);
            applyMeetingRepository.save(applyMeeting);
            return conference;
        }
    }

    public String disapproveConference(DisproveConferenceRequest request){
        ApplyMeeting applyMeeting = applyMeetingRepository.findByFullName(request.getFullName());
        if (applyMeeting == null) {
            //会议申请表中没有此会议
            return "error";
        } else {
            applyMeeting.setReviewStatus(3);
            applyMeetingRepository.save(applyMeeting);
            return "success";
        }
    }

    public List<ApplyMeeting>  reviewConference(){
        List<ApplyMeeting> applyMeetings=applyMeetingRepository.findAllByReviewStatus(1);
        if(applyMeetings==null){
            return null;
        }
        else{
            return applyMeetings;
        }
    }
}
