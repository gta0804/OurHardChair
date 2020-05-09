package fudan.se.lab2.controller;

import fudan.se.lab2.repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

//import javax.xml.ws.Response;

@SpringBootTest
public class PCMemberControllerTest {
    @Autowired
    PCMemberController pcMemberController;
    String token="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTU4ODk5OTk0NiwiZXhwIjoxNTg5MDE3OTQ2fQ.5lwt5mWjuFX-OjB1mkeJFsFWYvuFIiBNhw7TfM_Us9R3TCmVLsAzlwBtQW8DqiurO9_uGXwQK3aRCO7gzatyPQ";
    private MockHttpServletRequest request;

    @Autowired
    private ConferenceRepository conferenceRepository;

//    @Test
//    void search(){
//        Date date=new Date();
//        Conference conference =new Conference((long)1,date.toString(),date.toString(),date.toString(),date.toString(),date.toString(),date.toString(),1);
//        conferenceRepository.save(conference);
//        request = new MockHttpServletRequest();
//        request.setCharacterEncoding("UTF-8");
//        request.addHeader("Authorization", token);
//        SearchUserRequest searchUserRequest=new SearchUserRequest();
//
//        searchUserRequest.setFull_name(date.toString());
//        searchUserRequest.setSearch_key("tyghinjk");
//        SearchResponse searchResponse=new SearchResponse(date.toString(),date.toString(),date.toString(),date.toString(),date.toString(),1);
//        Assert.isTrue(searchResponse.getIs_open_submission()==1);
//        ShowSubmissionResponse showSubmissionResponse=new ShowSubmissionResponse(date.toString(),date.toString(),date.toString(),date.toString(),(long)1);
//        Assert.isTrue(showSubmissionResponse.getIs_open_submission()==(long)1);
//        RelatedConferenceResponse relatedConferenceResponse=new RelatedConferenceResponse(date.toString(),date.toString(),date.toString(),date.toString(),date.toString(),date.toString(),1,date.toString(),1);
//        Assert.isTrue(relatedConferenceResponse.getIs_open_submission()==1);
//        ResponseEntity<HashMap<String,Object>> responseEntity=pcMemberController.search(searchUserRequest,request);
//        Assert.isTrue(responseEntity.getBody().get("message").equals("success"));
//        conferenceRepository.delete(conference);
//    }

}
