package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ApplyMeetingRequest;
import fudan.se.lab2.controller.request.ApproveConferenceRequest;
import fudan.se.lab2.controller.request.DisapproveConferenceRequest;
import fudan.se.lab2.domain.ApplyMeeting;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.ApplyConferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin()
@RestController
public class ApplyConferenceController {
    @Autowired
    private ApplyConferenceService applyConferenceService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(ApplyConferenceController.class);

    @Autowired
    public ApplyConferenceController(ApplyConferenceService applyConferenceService){
        this.applyConferenceService=applyConferenceService;
    }

    /**
     * @Description: 直接展示所有会议，只需要token即可
     * @Param: [httpServletRequest]
     * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
     * @Author: Shen Zhengyu
     * @Date: 2020/4/8
     **/
    @CrossOrigin(origins = "*")
    @PostMapping("/AllConferences")
    public ResponseEntity<HashMap<String,Object>> showAllConference(HttpServletRequest httpServletRequest){
        logger.debug("Show all the conferences");
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String chairName = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getFullName();
        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = applyConferenceService.showAllConference();
        List<responseConference1> responseConferences = new ArrayList<>();
        for (Conference conference : conferences) {
            responseConference1 response = new responseConference1(conference.getId(),conference.getFullName(),conference.getAbbreviation(),conference.getHoldingPlace(),conference.getHoldingTime(),conference.getSubmissionDeadline(),conference.getReviewReleaseDate(),(Integer)2,chairName,conference.getIsOpenSubmission());
            responseConferences.add(response);
        }
        map.put("message","获取所有会议申请成功");
        map.put("token",token);
        map.put("meetings",responseConferences);
        return ResponseEntity.ok(map);
    }

    /*
    receive meeting application from frontend
 */
    @CrossOrigin(origins = "*")
    @RequestMapping("/ApplyConference")
    public ResponseEntity<HashMap<String,Object>> applyMeeting(HttpServletRequest httpServletRequest, @RequestBody ApplyMeetingRequest request){
        String token= httpServletRequest.getHeader("Authorization").substring(7);
        Long id= userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();
        String chairName = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getUsername();
        logger.debug("ApplyMeetingForm: " + request.toString());
        HashMap<String,Object> map = new HashMap();
        ApplyMeeting applyMeeting = applyConferenceService.applyMeeting(request,id);
        if (null == applyMeeting){
            map.put("message","会议申请失败，已有该会议");
            return ResponseEntity.ok(map);
        }else {
            map.put("token",token);
            map.put("message","success");
            map.put("user id",applyMeeting.getApplicantId());
            map.put("short_name",applyMeeting.getAbbreviation());
            map.put("full_name",applyMeeting.getFullName());
            map.put("holdingTime",applyMeeting.getHoldingTime());
            map.put("holdingPlace",applyMeeting.getHoldingPlace());
            map.put("submissionDeadline",applyMeeting.getSubmissionDeadline());
            map.put("reviewReleaseDate",applyMeeting.getReviewReleaseDate());
            map.put("reviewStatus",applyMeeting.getReviewStatus());
            return ResponseEntity.ok(map);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ReviewConference")
    public ResponseEntity<HashMap<String,Object>> reviewConference(HttpServletRequest httpServletRequest){

        logger.debug("reviewConference");
        HashMap<String,Object> map=new HashMap<>();
        List<ApplyMeeting> applyMeetings = applyConferenceService.reviewConference();
        String token= httpServletRequest.getHeader("Authorization").substring(7);

        if(null==applyMeetings){
            map.put("message","拉取待审核会议失败");
        }
        else{
            map.put("message","拉取待审核会议成功");
            map.put("meetings",applyMeetings);
            map.put("token",token);
        }
        return ResponseEntity.ok(map);

    }
    /*
      accept conference request from frontend
   */
    @CrossOrigin(origins = "*")
    @PostMapping("/ApproveConference")
    public ResponseEntity<HashMap<String,Object>> approveConference(@RequestBody ApproveConferenceRequest request,HttpServletRequest httpServletRequest){ ;
        String token= httpServletRequest.getHeader("Authorization").substring(7);
        logger.debug("approve conference"+request.toString());
        HashMap<String,Object> map = new HashMap<>();
        System.out.println("request: "+request.getFullName());
        Conference conference = applyConferenceService.approveConference(request);
        if(null == conference){
            map.put("message","批准会议申请失败，会议表找不到该会议");
        }
        else{
            map.put("token",token);
            map.put("message","批准会议成功");
        }
        return ResponseEntity.ok(map);
    }

    /*
    receive reject conference request from frontend
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/DisapproveConference")
    public ResponseEntity<HashMap<String,Object>> disapproveConference(@RequestBody DisapproveConferenceRequest request,HttpServletRequest httpServletRequest){
        logger.debug("disapprove conference"+request.toString());
        HashMap<String,Object> map=new HashMap<>();
        String message = applyConferenceService.disapproveConference(request);
        String token= httpServletRequest.getHeader("Authorization").substring(7);
        if(message.equals("error")){
            map.put("token",token);
            map.put("message","驳回会议申请失败");
        }
        else{
            map.put("message","驳回会议申请成功");
            map.put("token",token);
        }
        return ResponseEntity.ok(map);
    }
    public static class responseConference1{
        public Long conference_id;
        public String full_name;
        public String short_name;
        public String place;
        public String start_date;
        public String deadline_date;
        public String release_date;
        public Integer status;
        public String chair_name;
        public Integer is_open_submission;
        responseConference1(Long conference_id,String full_name,String short_name,String place,String start_date,String deadline_date,String release_date,Integer status,String chair_name,Integer is_open_submission){
            this.conference_id=conference_id;
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
