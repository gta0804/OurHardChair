package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.SearchUserRequest;
import fudan.se.lab2.controller.response.SearchResponse;
import fudan.se.lab2.controller.response.ShowSubmissionResponse;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.ConferenceRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;

//import javax.xml.ws.Response;

@SpringBootTest
public class PCMemberControllerTest {
<<<<<<< HEAD
//    @Autowired
//    PCMemberController pcMemberController;
//    String token="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTU4NjY4MDYzMCwiZXhwIjoxNTg2Njk4NjMwfQ.laMZ1U8mDn53ig9AG4sw23XKMasthIqCd0YDnfV9K9GTICGprAdthhhYj0RZqmMjb09iGd5-OsznQRudUJBmKw";
//    private MockHttpServletRequest request;
//
//    @Autowired
//    private ConferenceRepository conferenceRepository;
=======
    @Autowired
    PCMemberController pcMemberController;
    private MockHttpServletRequest request;

    @Autowired
    private ConferenceRepository conferenceRepository;
>>>>>>> 8a95fedf54cbfb724dda41d01ad28b77ff7da3b7

    @Autowired
    private AuthController authController;

    @Test
   void search(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        Date date=new Date();
        Conference conference =new Conference((long)1,date.toString(),date.toString(),date.toString(),date.toString(),date.toString(),date.toString(),1);
       conferenceRepository.save(conference);
       request = new MockHttpServletRequest();
       request.setCharacterEncoding("UTF-8");
       request.addHeader("Authorization", token);
       SearchUserRequest searchUserRequest=new SearchUserRequest();
        searchUserRequest.setFull_name(date.toString());
       searchUserRequest.setSearch_key("tyghinjk");
      SearchResponse searchResponse=new SearchResponse(date.toString(),date.toString(),date.toString(),date.toString(),date.toString(),1);
      Assert.isTrue(searchResponse.getStatus()==1);
       ShowSubmissionResponse showSubmissionResponse=new ShowSubmissionResponse(date.toString(),date.toString(),date.toString(),date.toString(),(long)1);
      Assert.isTrue(showSubmissionResponse.getStatus()==(long)1);
   }

}
