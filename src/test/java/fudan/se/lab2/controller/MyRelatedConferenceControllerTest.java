package fudan.se.lab2.controller;

import fudan.se.lab2.Lab2Application;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.OpenManuscriptReviewRequest;
import fudan.se.lab2.controller.request.OpenSubmissionRequest;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.ConferenceRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;
import java.util.HashMap;

@SpringBootTest(classes= Lab2Application.class)
public class MyRelatedConferenceControllerTest {
    @Autowired
    private AuthController authController;

    @Autowired
    private MyRelatedConferenceController myRelatedConferenceController;


    @Autowired
    private ConferenceRepository conferenceRepository;

    private MockHttpServletRequest request;

    @Test
    void openSubmissionTest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testA");
        loginRequest.setPassword("123456");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        OpenSubmissionRequest openSubmissionRequest=new OpenSubmissionRequest();
        openSubmissionRequest.setFull_name("testMeeting1");
        ResponseEntity<HashMap<String,Object>> responseEntity1=myRelatedConferenceController.openSubmission(request,openSubmissionRequest);
        Assert.isTrue(responseEntity1.getBody().get("message").equals("开启成功"));
        ResponseEntity<HashMap<String,Object>> responseEntity2=myRelatedConferenceController.openSubmission(request,openSubmissionRequest);
        Assert.isTrue(responseEntity2.getBody().get("message").equals("开启失败"));
    }

   @Test
  void openManuscriptReview(){
       LoginRequest loginRequest = new LoginRequest();
       loginRequest.setUsername("admin");
       loginRequest.setPassword("password");
       String token=(String)authController.login(loginRequest).getBody().get("token");
       request = new MockHttpServletRequest();
       request.setCharacterEncoding("UTF-8");
       request.addHeader("Authorization","Bearer " + token);
       Date date=new Date();
       Conference conference=conferenceRepository.findByFullName(date.toString());
       if(conference==null){
           conference=new Conference((long)1,"shortName",date.toString(),"holdingPlace",new Date().toString(),new Date().toString(),new Date().toString());
           conference.setReviewStatus(2);
           conferenceRepository.save(conference);
       }
       OpenManuscriptReviewRequest openManuscriptReviewRequest=new OpenManuscriptReviewRequest();
       openManuscriptReviewRequest.setConference_id(conference.getId());
       openManuscriptReviewRequest.setAllocationStrategy(2);
       String message=(String)myRelatedConferenceController.openManuscriptReview(request,openManuscriptReviewRequest).getBody().get("message");
       Assert.isTrue(message.equals("PCMember数量少于2个，您不能开启投稿"));
       conferenceRepository.delete(conference);

    }


}
