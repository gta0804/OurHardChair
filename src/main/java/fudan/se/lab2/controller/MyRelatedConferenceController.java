package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.OpenManuscriptReviewRequest;
import fudan.se.lab2.controller.request.OpenSubmissionRequest;
import fudan.se.lab2.controller.request.ShowSubmissionRequest;
import fudan.se.lab2.controller.response.AllConferenceResponse;
import fudan.se.lab2.controller.response.ConferenceForChairResponse;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private UpdateService updateService;

    Logger logger = LoggerFactory.getLogger(MyRelatedConferenceController.class);
    @Autowired
    public MyRelatedConferenceController(MyRelatedConferenceService myRelatedConferenceService){
        this.myRelatedConferenceService = myRelatedConferenceService;
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
        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = myRelatedConferenceService.showAllConference();
        List<AllConferenceResponse> responseConferences =myRelatedConferenceService.getResponses(conferences);
        map.put("message","获取所有会议申请成功");
        map.put("token",token);
        map.put("meetings",responseConferences);
        updateService.update(logger);
        return ResponseEntity.ok(map);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/ConferenceForChair")
    public ResponseEntity<HashMap<String,Object>> showAllConferenceForChair(HttpServletRequest httpServletRequest){
        logger.debug("Show all the conferences for chair");
        //首先加载所有已申请的会议
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        HashMap<String,Object> map = new HashMap<>();
        List<ConferenceForChairResponse> conferences = myRelatedConferenceService.showAllConferenceForChair();
        //再加载所有申请过的会议
        //开始合并
        List<AllConferenceResponse> responseConferences = myRelatedConferenceService.getResponses2(conferences);
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
            List<AllConferenceResponse> responseConferences = myRelatedConferenceService.getResponses(conferences);
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
        HashMap<String,Object> map = new HashMap<>();
        List<Conference> conferences = myRelatedConferenceService.showAllConferenceForAuthor();
        if(conferences==null){
            map.put("message","获取所有我主持的会议申请失败");
            return ResponseEntity.ok(map);
        }
        List<AllConferenceResponse> responseConferences = myRelatedConferenceService.getResponses(conferences);
        map.put("message","获取所有我投稿的会议申请成功");
        map.put("token",token);
        map.put("meetings",responseConferences);
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
            map.put("token",token);
        }else{
            map.put("message","开启失败");
        }
        map.put("chairName",chairName);
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
        return ResponseEntity.ok(map);
    }

    @CrossOrigin("*")
    @PostMapping("/openManuscriptReview")
    public ResponseEntity<HashMap<String,Object>> openManuscriptReview(HttpServletRequest httpServletRequest,@RequestBody OpenManuscriptReviewRequest request){
        logger.debug("openManuscriptReview"+request.toString());
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        HashMap<String,Object> map=new HashMap<>();
        String message=myRelatedConferenceService.openManuscriptReview(request);
        if(message.equals("开启投稿成功")){
            map.put("token",token);
        }
        map.put("message",message);
        return ResponseEntity.ok(map);

    }


}
