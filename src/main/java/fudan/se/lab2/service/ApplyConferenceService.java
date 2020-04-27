package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.ApplyMeetingRequest;
import fudan.se.lab2.controller.request.ReviewConferenceRequest;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.repository.ApplyMeetingRepository;
import fudan.se.lab2.repository.ConferenceRepository;
import fudan.se.lab2.repository.MessageRepository;
import fudan.se.lab2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ApplyConferenceService {
    private final String RESPONSE="ApplyConferenceResponse";
    @Autowired
    private ApplyMeetingRepository applyMeetingRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    public  ApplyConferenceService(ApplyMeetingRepository applyMeetingRepository,ConferenceRepository conferenceRepository,UserRepository userRepository,MessageRepository messageRepository){
        this.applyMeetingRepository=applyMeetingRepository;
        this.conferenceRepository=conferenceRepository;
        this.userRepository=userRepository;
        this.messageRepository=messageRepository;
    }

    public ApplyMeeting applyMeeting(ApplyMeetingRequest request, Long id){
        if(null!=applyMeetingRepository.findByFullName(request.getFullName())){
            System.out.println("会议全称重复");
            return null;
        }
        else{
            List<Topic> topics = new ArrayList<>();
            for (String topic : request.getTopics()) {
                topics.add(new Topic(topic));
            }
            ApplyMeeting applyMeeting=new ApplyMeeting(id,(long)1,request.getAbbreviation(),request.getFullName(),request.getHoldingTime(),request.getHoldingPlace(),request.getSubmissionDeadline(),request.getReviewReleaseDate(),1,topics);
            applyMeetingRepository.save(applyMeeting);
            return applyMeeting;
        }
    }

    public Conference approveConference(ReviewConferenceRequest request) {
        System.out.println(request.getFullName());
        ApplyMeeting applyMeeting = applyMeetingRepository.findByFullName(request.getFullName());
        if (applyMeeting == null) {
            System.out.println("会议申请表中没有此会议");
            //会议申请表中没有此会议
            return null;
        } else {

            Conference conference = new Conference(applyMeeting.getApplicantId(), applyMeeting.getAbbreviation(), applyMeeting.getFullName(), applyMeeting.getHoldingPlace(), applyMeeting.getHoldingTime(), applyMeeting.getSubmissionDeadline(), applyMeeting.getReviewReleaseDate(), 1,applyMeeting.getTopics());
            conferenceRepository.save(conference);
            //会议通过，置为2
            applyMeeting.setReviewStatus(2);
            applyMeetingRepository.save(applyMeeting);
            User user=userRepository.findById(applyMeeting.getApplicantId()).orElse(null);
            //错误：没有这个用户

            if(user==null){
                System.out.println("在生成消息时没有找到用户");
                return null;
            }
            Message message=new Message("admin",user.getUsername(),applyMeeting.getFullName(),"管理员通过了你的会议请求",RESPONSE,1);
            messageRepository.save(message);
            return conference;
        }
    }

    public String disapproveConference(ReviewConferenceRequest request){
        ApplyMeeting applyMeeting = applyMeetingRepository.findByFullName(request.getFullName());
        if (applyMeeting == null) {
            //会议申请表中没有此会议
            return "error";
        } else {
            applyMeeting.setReviewStatus(3);
            applyMeetingRepository.save(applyMeeting);
            User user=userRepository.findById(applyMeeting.getApplicantId()).orElse(null);
            //错误：没有这个用户
            if(user==null){
                System.out.println("在生成消息时没有找到用户");
                return "error";
            }
            Message message=new Message("admin",user.getUsername(),applyMeeting.getFullName(),"管理员拒绝了你的会议请求",RESPONSE,1);
            messageRepository.save(message);
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

    public ArrayList<Conference>  showAllConference(){
        ArrayList<Conference> conferences = (ArrayList<Conference>) conferenceRepository.findAll();
        return conferences;
    }

    public ArrayList<ApplyMeeting>  showAllApplyMeetingById(long id){
        ArrayList<ApplyMeeting> applyMeetings = (ArrayList<ApplyMeeting>)applyMeetingRepository.findAllByApplicantId(id);
        return applyMeetings;
    }
}
