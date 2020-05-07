package fudan.se.lab2.controller;

import fudan.se.lab2.service.ContributionService;
import fudan.se.lab2.service.MyRelatedConferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-06 14:05
 **/
@CrossOrigin()
@Controller
public class ResultController {
    Logger logger = LoggerFactory.getLogger(ResultController.class);

    @Autowired
    MyRelatedConferenceService myRelatedConferenceService;

    @Autowired
    ContributionService contributionService;


    @CrossOrigin("*")
    @PostMapping("/releaseReviewResult")
    public ResponseEntity<HashMap<String, Object>> releaseReviewResult(@RequestParam("conference_id") Long conference_id,@RequestParam("userId") Long userId,HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        HashMap<String,Object> hashMap = new HashMap<>();
        String result = myRelatedConferenceService.releaseReviewResult(conference_id,userId);
        hashMap.put("token",token);
        hashMap.put("message",result);
        return ResponseEntity.ok(hashMap);
    }

    @CrossOrigin("*")
    @PostMapping("/viewReviewResult")
    public ResponseEntity<HashMap<String, Object>> viewReviewResult(@RequestParam("conference_id") Long conference_id,@RequestParam("userId") Long userId,HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        HashMap<String,Object> hashMap = contributionService.viewReviewResult(conference_id,userId);
        hashMap.put("token",token);
        return ResponseEntity.ok(hashMap);
    }

    }
