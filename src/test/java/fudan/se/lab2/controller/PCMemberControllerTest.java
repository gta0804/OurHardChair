package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.SearchUserRequest;
import fudan.se.lab2.controller.response.RelatedConferenceResponse;
import fudan.se.lab2.controller.response.SearchResponse;
import fudan.se.lab2.controller.response.ShowSubmissionResponse;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.domain.Topic;
import fudan.se.lab2.repository.ConferenceRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import java.util.*;

import javax.xml.ws.Response;

@SpringBootTest
public class PCMemberControllerTest {
    @Autowired
    PCMemberController pcMemberController;
    String token="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTU4NjY4MDYzMCwiZXhwIjoxNTg2Njk4NjMwfQ.laMZ1U8mDn53ig9AG4sw23XKMasthIqCd0YDnfV9K9GTICGprAdthhhYj0RZqmMjb09iGd5-OsznQRudUJBmKw";
    private MockHttpServletRequest request;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Test
    void search(){
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
        RelatedConferenceResponse relatedConferenceResponse=new RelatedConferenceResponse(date.toString(),date.toString(),date.toString(),date.toString(),date.toString(),date.toString(),1,date.toString(),1);
        Assert.isTrue(relatedConferenceResponse.getStatus()==1);
        ResponseEntity<HashMap<String,Object>> responseEntity=pcMemberController.search(searchUserRequest,request);
        Assert.isTrue(responseEntity.getBody().get("message").equals("success"));
        conferenceRepository.delete(conference);
    }

}
