package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.ApplyMeetingRequest;
import fudan.se.lab2.controller.request.ReviewConferenceRequest;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ApplyConferenceService {
    private final String RESPONSE="ApplyConferenceResponse";

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    public  ApplyConferenceService(ConferenceRepository conferenceRepository,UserRepository userRepository,MessageRepository messageRepository){
        this.conferenceRepository=conferenceRepository;
        this.userRepository=userRepository;
        this.messageRepository=messageRepository;
    }

    public Conference applyMeeting(ApplyMeetingRequest request, Long id){
        if(null!=conferenceRepository.findByFullName(request.getFullName())){
            System.out.println("会议全称重复");
            return null;
        }
        Conference conference=new Conference(id,request.getAbbreviation(),request.getFullName(),request.getHoldingTime(),request.getHoldingPlace(),request.getSubmissionDeadline(),request.getReviewReleaseDate(),1);
        Set<Topic> topics = new HashSet<>();
        for (String topicName : request.getTopics()) {
            Topic topic=topicRepository.findByTopic(topicName);
            if(topic==null){
                topic=new Topic(topicName);
            }
            topics.add(topic);
            Set<Conference> conferences=topic.getConference();
            conferences.add(conference);
            topic.setConference(conferences);
            topicRepository.save(topic);
        }
        conference.setTopics(topics);
        conferenceRepository.save(conference);
        return conference;
    }

    public Conference approveConference(ReviewConferenceRequest request) {
        Conference conference = conferenceRepository.findByFullName(request.getFullName());
        if (conference == null) {
            System.out.println("会议申请表中没有此会议");
            //会议申请表中没有此会议
            return null;
        }
        conference.setReviewStatus(2);
        conferenceRepository.save(conference);
        //会议通过，置为2
        User user=userRepository.findById(conference.getChairId()).orElse(null);
        //错误：没有这个用户
        if(user==null){
            System.out.println("在生成消息时没有找到用户");
            return null;
        }
        //保存消息
        Message message=new Message("admin",user.getUsername(),conference.getFullName(),"管理员通过了你的会议请求",RESPONSE,1);
        messageRepository.save(message);
        for(Topic topic:conference.getTopics()){
            if(topicRepository.findByTopic(topic.getTopic())==null){
                return null;
            }
        }
        return conference;
    }

    public String disapproveConference(ReviewConferenceRequest request){
        Conference conference = conferenceRepository.findByFullName(request.getFullName());
        if (conference == null) {
            //会议申请表中没有此会议
            return "error";
        } else {
            conference.setReviewStatus(3);
            conferenceRepository.save(conference);
            User user=userRepository.findById(conference.getChairId()).orElse(null);
            //错误：没有这个用户
            if(user==null){
                System.out.println("在生成消息时没有找到用户");
                return "error";
            }
            Message message=new Message("admin",user.getUsername(),conference.getFullName(),"管理员拒绝了你的会议请求",RESPONSE,1);
            messageRepository.save(message);
            return "success";
        }
    }

    public List<Conference>  reviewConference(){
        List<Conference> conferences=conferenceRepository.findAllByReviewStatus(1);
        if(conferences==null){
            return null;
        }
        return conferences;
    }

    public List<Conference>  showAllConference(){
        List<Conference> conferences =  conferenceRepository.findAllByReviewStatus(2);
        return conferences;
    }

}
