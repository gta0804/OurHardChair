package fudan.se.lab2.controller;

import fudan.se.lab2.domain.ApplyMeeting;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.ApplyConferenceService;
import fudan.se.lab2.service.MyRelatedConferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin()
@RestController
public class MyRelatedConferenceController {
    @Autowired
    private MyRelatedConferenceService myRelatedConferenceService;

    @Autowired
    private ApplyConferenceService applyConferenceService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(MyRelatedConferenceController.class);
    @Autowired
    public MyRelatedConferenceController(MyRelatedConferenceService myRelatedConferenceService){
        this.myRelatedConferenceService = myRelatedConferenceService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ConferenceForChair")
    public ResponseEntity<HashMap<String,Object>> showAllConferenceForChair(HttpServletRequest httpServletRequest){
        logger.debug("Show all the conferences for chair");
        //首先加载所有已申请的会议
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        Long id = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();
        String chairName = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getFullName();
        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = myRelatedConferenceService.showAllConferenceForChair(id);
        //再加载所有申请过的会议
        List<ApplyMeeting> applyMeetings = applyConferenceService.showAllApplyMeetingById(id);
        //开始合并
        List<responseConference> responseConferences = new ArrayList<>();
        for (Conference conference : conferences) {
            responseConference response = new responseConference(conference.getFullName(),conference.getAbbreviation(),conference.getHoldingPlace(),conference.getHoldingTime(),conference.getSubmissionDeadline(),conference.getReviewReleaseDate(),(Integer)2,chairName,conference.getIsOpenSubmission());
            responseConferences.add(response);
        }
        for (ApplyMeeting applyMeeting : applyMeetings) {
            Integer status;
            if (applyMeeting.getReviewStatus() == 3)
            {
                status = (Integer)3;
            }
            else{
                status = (Integer)1;
            }
            if (applyMeeting.getReviewStatus() != 2){
                responseConference response = new responseConference(applyMeeting.getFullName(),applyMeeting.getAbbreviation(),applyMeeting.getHoldingPlace(),applyMeeting.getHoldingTime(),applyMeeting.getSubmissionDeadline(),applyMeeting.getReviewReleaseDate(),status,chairName,(Integer)1);
                responseConferences.add(response);
            }
        }
        map.put("message","获取所有我主持的会议申请成功");
        map.put("token",token);
        map.put("meetings",responseConferences);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ConferenceForPCMember")
    public ResponseEntity<HashMap<String,Object>> showAllConferenceForPCMember(HttpServletRequest httpServletRequest) {
        logger.debug("Show all the conferences for PC Member");
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        Long id = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();
        String chairName = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getFullName();
        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = myRelatedConferenceService.showAllConferenceForPCMember(id);
        if(conferences==null){
            map.put("message","获取所有我审稿的会议申请失败");
            return ResponseEntity.ok(map);
        }
        else{
            List<responseConference> responseConferences = new ArrayList<>();
            for (Conference conference : conferences) {
                responseConference response = new responseConference(conference.getFullName(),conference.getAbbreviation(),conference.getHoldingPlace(),conference.getHoldingTime(),conference.getSubmissionDeadline(),conference.getReviewReleaseDate(),(Integer)2,chairName,conference.getIsOpenSubmission());
                responseConferences.add(response);
            }
            map.put("message","获取所有我审稿的会议申请成功");
            map.put("token",token);
            map.put("meetings",responseConferences);
            return ResponseEntity.ok(map);
        }

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ConferenceForAuthor")
    public ResponseEntity<HashMap<String,Object>> showAllConferenceForAuthor(HttpServletRequest httpServletRequest) {
        logger.debug("Show all the conferences for Author");
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        Long id = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();
        String chairName = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getFullName();

        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = myRelatedConferenceService.showAllConferenceForAuthor(id);
        if(conferences==null){
            map.put("message","获取所有我主持的会议申请失败");
            return ResponseEntity.ok(map);
        }
        List<responseConference> responseConferences = new ArrayList<>();
        for (Conference conference : conferences) {
            responseConference response = new responseConference(conference.getFullName(),conference.getAbbreviation(),conference.getHoldingPlace(),conference.getHoldingTime(),conference.getSubmissionDeadline(),conference.getReviewReleaseDate(),(Integer)2,chairName,conference.getIsOpenSubmission());
            responseConferences.add(response);
        }
        map.put("message","获取所有我投稿的会议申请成功");
        map.put("token",token);
        map.put("meetings",responseConferences);
        return ResponseEntity.ok(map);
    }

    //定义返回的回应对象
    public static class responseConference{
        public String full_name;
        public String short_name;
        public String place;
        public String start_date;
        public String deadline_date;
        public String release_date;
        public Integer status;
        public String chair_name;
        public Integer is_open_submission;
        responseConference(String full_name,String short_name,String place,String start_date,String deadline_date,String release_date,Integer status,String chair_name,Integer is_open_submission){
            this.full_name = full_name;
            this.short_name = short_name;
            this.place = place;
            this.start_date = start_date;
            this.deadline_date = deadline_date;
            this.release_date = release_date;
            this.status = status;
            this.chair_name = chair_name;
            this.is_open_submission = is_open_submission;
        }
    }
}
