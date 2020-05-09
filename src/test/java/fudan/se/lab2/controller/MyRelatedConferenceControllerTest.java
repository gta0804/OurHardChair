package fudan.se.lab2.controller;

import fudan.se.lab2.Lab2Application;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.OpenManuscriptReviewRequest;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.ConferenceRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;

@SpringBootTest(classes= Lab2Application.class)
public class MyRelatedConferenceControllerTest {
    @Autowired
    private AuthController authController;

    @Autowired
    private MyRelatedConferenceController myRelatedConferenceController;

    @Autowired
    private ApplyConferenceController applyConferenceController;

    @Autowired
    private ConferenceRepository conferenceRepository;

    private MockHttpServletRequest request;

//    @Test
//    void openManuscriptReview(){
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setUsername("admin");
//        loginRequest.setPassword("password");
//        String token=(String)authController.login(loginRequest).getBody().get("token");
//        request = new MockHttpServletRequest();
//        request.setCharacterEncoding("UTF-8");
//        request.addHeader("Authorization","Bearer " + token);
//        Date date=new Date();
//        Conference temp=conferenceRepository.findByFullName(date.toString());
//        Assert.isNull(temp);
//        Conference conference=new Conference((long)1,"shortName",date.toString(),"holdingPlace",new Date().toString(),new Date().toString(),new Date().toString(),2);
//        conference.setReviewStatus(2);
//        conferenceRepository.save(conference);
//        OpenManuscriptReviewRequest openManuscriptReviewRequest=new OpenManuscriptReviewRequest();
//        openManuscriptReviewRequest.setConference_id(conference.getId());
//        openManuscriptReviewRequest.setAllocationStrategy(2);
//
//        String message=(String)myRelatedConferenceController.openManuscriptReview(request,openManuscriptReviewRequest).getBody().get("message");
//        Assert.isTrue(message.equals("PCMember数量少于2个，您不能开启投稿"));
//        conferenceRepository.delete(conference);
//
//    }


}
