package fudan.se.lab2.controller;

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
    @CrossOrigin("*")
    @PostMapping("/releaseReviewResult")
    public ResponseEntity<HashMap<String, Object>> releaseReviewResult(@RequestParam("conference_id") Long conference_id,@RequestParam("userId") Long userId,HttpServletRequest httpServletRequest) {
        HashMap<String,Object> hashMap = new HashMap<>();
        String result = myRelatedConferenceService.releaseReviewResult(conference_id,userId);
        hashMap.put("message",result);
        return ResponseEntity.ok(hashMap);
    }

}
