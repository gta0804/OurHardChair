package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ApplyMeetingRequest;
import fudan.se.lab2.controller.request.ApproveConferenceRequest;
import fudan.se.lab2.controller.request.DisproveConferenceRequest;
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

    /*
    receive meeting application from frontend
 */
    @CrossOrigin(origins = "*")
    @RequestMapping("/ApplyConference")
    public ResponseEntity<HashMap<String,Object>> applyMeeting(HttpServletRequest httpServletRequest, @RequestBody ApplyMeetingRequest request){
        System.out.println("rawToken" + httpServletRequest.getHeader("Authorization"));
        String token= httpServletRequest.getHeader("Authorization").substring(7);
        System.out.println("经过处理的token是" + token);
        System.out.println("找到名字" + jwtTokenUtil.getUsernameFromToken(token));
        System.out.println("找到Email" + userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getEmail());
        System.out.println(request.getFullName() + "是本次会议的fullname");
        Long id= userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();
        logger.debug("ApplyMeetingForm: " + request.toString());
        HashMap<String,Object> map = new HashMap();
        ApplyMeeting applyMeeting=applyConferenceService.applyMeeting(request,id);
        if (null == applyMeeting){
            map.put("message","会议申请失败，已有该会议");
            return ResponseEntity.ok(map);
        }else {
            map.put("token",token);
            map.put("message","success");
            map.put("user id",applyMeeting.getApplicantId());
            map.put("verifier id",applyMeeting.getVerifierId());
            map.put("abbreviation",applyMeeting.getAbbreviation());
            map.put("fullName",applyMeeting.getFullName());
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
    public ResponseEntity<HashMap<String,Object>> reviewConference(){
        logger.debug("reviewConference");
        HashMap<String,Object> map=new HashMap<>();
        List<ApplyMeeting> applyMeetings=applyConferenceService.reviewConference();
        if(null==applyMeetings){
            map.put("message","拉取待审核会议失败");
        }
        else if(applyMeetings.size()==0){
            map.put("message","没有待审核的会议");
        }
        else{
            map.put("message","拉取待审核会议成功");
            map.put("meetings",applyMeetings);
        }
        return ResponseEntity.ok(map);

    }
    /*
      accept conference request from frontend
   */
    @CrossOrigin(origins = "*")
    @PostMapping("/ApproveConference")
    public ResponseEntity<HashMap<String,Object>> approveConference(@RequestBody ApproveConferenceRequest request){
        logger.debug("approve conference"+request.toString());
        HashMap<String,Object> map=new HashMap<>();
        Conference conference=applyConferenceService.approveConference(request);
        if(null==conference){
            map.put("message","批准会议申请失败，会议表找不到该会议");
        }
        else{
            map.put("message","批准会议成功");
        }
        return ResponseEntity.ok(map);
    }

    /*
    receive reject conference request from frontend
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/DisapproveConference")
    public ResponseEntity<HashMap<String,Object>> disapproveConference(@RequestBody DisproveConferenceRequest request){
        logger.debug("disapprove conference"+request.toString());
        HashMap<String,Object> map=new HashMap<>();
        String message=applyConferenceService.disapproveConference(request);
        if(message.equals("error")){
            map.put("message","驳回会议申请失败，会议表找不到该会议");
        }
        else{
            map.put("message","驳回会议申请成功");
        }
        return ResponseEntity.ok(map);
    }


}
