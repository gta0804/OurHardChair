package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.OpenSubmissionRequest;
import fudan.se.lab2.controller.request.ShowSubmissionRequest;
import fudan.se.lab2.controller.response.RelatedConferenceResponse;
import fudan.se.lab2.controller.response.ShowSubmissionResponse;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.ApplyConferenceService;
import fudan.se.lab2.service.MyRelatedConferenceService;
import fudan.se.lab2.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private UpdateService updateService;

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
        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = myRelatedConferenceService.showAllConferenceForChair();
        //再加载所有申请过的会议
        //开始合并
        List<RelatedConferenceResponse> responseConferences = new ArrayList<>();
        for (Conference conference : conferences) {
            RelatedConferenceResponse response = new RelatedConferenceResponse(conference.getId(),conference.getFullName(),conference.getAbbreviation(),conference.getHoldingPlace(),conference.getHoldingTime(),conference.getSubmissionDeadline(),conference.getReviewReleaseDate(),conference.getReviewStatus(),SecurityContextHolder.getContext().getAuthentication().getName(),conference.getIsOpenSubmission(),conference.getTopics());
            responseConferences.add(response);
        }
        updateService.update(logger);
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
        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = myRelatedConferenceService.showAllConferenceForPCMember();
        if(conferences==null){
            map.put("message","获取所有我审稿的会议申请失败");
            return ResponseEntity.ok(map);
        }
        else{
            List<RelatedConferenceResponse> responseConferences = new ArrayList<>();
            for (Conference conference : conferences) {
                RelatedConferenceResponse response = new RelatedConferenceResponse(conference.getId(),conference.getFullName(),conference.getAbbreviation(),conference.getHoldingPlace(),conference.getHoldingTime(),conference.getSubmissionDeadline(),conference.getReviewReleaseDate(),(Integer)2,SecurityContextHolder.getContext().getAuthentication().getName(),conference.getIsOpenSubmission(),conference.getTopics());
                responseConferences.add(response);
            }
            map.put("message","获取所有我审稿的会议申请成功");
            map.put("token",token);
            map.put("meetings",responseConferences);
            updateService.update(logger);
            return ResponseEntity.ok(map);
        }

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ConferenceForAuthor")
    public ResponseEntity<HashMap<String,Object>> showAllConferenceForAuthor(HttpServletRequest httpServletRequest) {
        logger.debug("Show all the conferences for Author");
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = myRelatedConferenceService.showAllConferenceForAuthor();
        if(conferences==null){
            map.put("message","获取所有我主持的会议申请失败");
            return ResponseEntity.ok(map);
        }
        List<RelatedConferenceResponse> responseConferences = new ArrayList<>();
        for (Conference conference : conferences) {
            RelatedConferenceResponse response = new RelatedConferenceResponse(conference.getId(),conference.getFullName(),conference.getAbbreviation(),conference.getHoldingPlace(),conference.getHoldingTime(),conference.getSubmissionDeadline(),conference.getReviewReleaseDate(),(Integer)2,SecurityContextHolder.getContext().getAuthentication().getName(),conference.getIsOpenSubmission(),conference.getTopics());
            responseConferences.add(response);
        }
        map.put("message","获取所有我投稿的会议申请成功");
        map.put("token",token);
        map.put("meetings",responseConferences);
        updateService.update(logger);
        return ResponseEntity.ok(map);
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/openSubmission")
    public ResponseEntity<HashMap<String,Object>> openSubmission(HttpServletRequest httpServletRequest, @RequestBody OpenSubmissionRequest openSubmissionRequest) {
        logger.debug("Open submission: "+ openSubmissionRequest.toString());
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String chairName = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getFullName();

        HashMap<String,Object> map = new HashMap<>();
        boolean status = myRelatedConferenceService.openSubmission(openSubmissionRequest.getFull_name());
        if(status){
            map.put("message","开启成功");
            return ResponseEntity.ok(map);
        }else{
            map.put("message","开启失败");
        }
        map.put("token",token);
        map.put("chairName",chairName);
        updateService.update(logger);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin("*")
    @PostMapping("/showMySubmission")
    public ResponseEntity<HashMap<String,Object>> showMySubmission(@RequestBody ShowSubmissionRequest request,HttpServletRequest httpServletRequest){
        logger.debug("showMySubmission: "+request.toString());
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        HashMap<String,Object> map=new HashMap<>();
        List<ShowSubmissionResponse> responses=myRelatedConferenceService.showSubmission(request);
        map.put("message","success");
        map.put("token",token);
        map.put("submissions",responses);
        updateService.update(logger);
        return ResponseEntity.ok(map);
    }

}
