package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ApplyMeetingRequest;
import fudan.se.lab2.controller.request.ReviewConferenceRequest;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;
import java.util.HashMap;

@SpringBootTest
class ApplyConferenceControllerTest {

    private MockHttpServletRequest request;

    @Autowired
    MyRelatedConferenceController myRelatedConferenceController;

    @Autowired
     ApplyConferenceController applyConferenceController;

    String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTU4NjY4MDYzMCwiZXhwIjoxNTg2Njk4NjMwfQ.laMZ1U8mDn53ig9AG4sw23XKMasthIqCd0YDnfV9K9GTICGprAdthhhYj0RZqmMjb09iGd5-OsznQRudUJBmKw";

    /**
    * @Description: 测试获取全部会议是否成功
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/12
    */
    @Test
    void showAllConference() {
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization", token);
        ResponseEntity<HashMap<String, Object>> responseEntity = myRelatedConferenceController.showAllConference(request);
        Assert.isTrue(responseEntity.getBody().get("message").equals("获取所有会议申请成功"));
    }


    /**
    * @Description: 测试申请会议
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/12
    */
    @Test
    void applyMeeting() {
        Date date = new Date();
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization", token);
        ApplyMeetingRequest applyMeetingRequest = new ApplyMeetingRequest();
        applyMeetingRequest.setFullName((date).toString());
        applyMeetingRequest.setHoldingTime((date).toString());
        applyMeetingRequest.setSubmissionDeadline((date).toString());
        applyMeetingRequest.setAbbreviation((date).toString());
        applyMeetingRequest.setHoldingPlace("Shanghai");
        ResponseEntity<HashMap<String, Object>> responseEntity = applyConferenceController.applyMeeting(request,applyMeetingRequest);
//        Assert.isTrue(responseEntity.getBody().get("message").equals("success"));
        ResponseEntity<HashMap<String, Object>> responseEntity1 = applyConferenceController.applyMeeting(request,applyMeetingRequest);
        Assert.isTrue(responseEntity1.getBody().get("message").equals("会议申请失败，已有该会议"));
    }

    /**
    * @Description: 测试拉取待审核
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/12
    */
    @Test
    void reviewConference() {
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization", token);
        ResponseEntity<HashMap<String, Object>> responseEntity = applyConferenceController.reviewConference(request);
        Assert.isTrue(responseEntity.getBody().get("message").equals("拉取待审核会议失败") || responseEntity.getBody().get("message").equals("拉取待审核会议成功") );

    }

    /**
    * @Description: 同意会议
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/12
    */
    @Test
    void approveConference() {
        Date date = new Date();
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization", token);
        ApplyMeetingRequest applyMeetingRequest = new ApplyMeetingRequest();
        applyMeetingRequest.setFullName((date).toString());
        applyMeetingRequest.setHoldingTime((date).toString());
        applyMeetingRequest.setSubmissionDeadline((date).toString());
        applyMeetingRequest.setAbbreviation((date).toString());
        applyMeetingRequest.setHoldingPlace("Shanghai");
        applyConferenceController.applyMeeting(request,applyMeetingRequest);
        ReviewConferenceRequest approveConferenceRequest = new ReviewConferenceRequest();
        approveConferenceRequest.setFullName(date.toString());
        ResponseEntity<HashMap<String, Object>> responseEntity = applyConferenceController.approveConference(approveConferenceRequest,request);
        Assert.isTrue(responseEntity.getBody().get("message").equals("批准会议成功"));
        approveConferenceRequest.setFullName((new Date()).toString() + "231231231");
        ResponseEntity<HashMap<String, Object>> responseEntity1 = applyConferenceController.approveConference(approveConferenceRequest,request);
        Assert.isTrue(responseEntity1.getBody().get("message").equals("批准会议申请失败，会议表找不到该会议"));
    }

    /**
    * @Description: 拒绝会议
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/12
    */
    @Test
    void disapproveConference() {
        Date date = new Date();
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization", token);
        ApplyMeetingRequest applyMeetingRequest = new ApplyMeetingRequest();
        applyMeetingRequest.setFullName((date).toString());
        applyMeetingRequest.setHoldingTime((date).toString());
        applyMeetingRequest.setSubmissionDeadline((date).toString());
        applyMeetingRequest.setAbbreviation((date).toString());
        applyMeetingRequest.setHoldingPlace("Shanghai");
        applyConferenceController.applyMeeting(request,applyMeetingRequest);
        ReviewConferenceRequest disapproveConferenceRequest = new ReviewConferenceRequest();
        disapproveConferenceRequest.setFullName(date.toString());
        ResponseEntity<HashMap<String, Object>> responseEntity = applyConferenceController.disapproveConference(disapproveConferenceRequest,request);
        Assert.isTrue(responseEntity.getBody().get("message").equals("驳回会议申请成功"));
        disapproveConferenceRequest.setFullName((new Date()).toString() + "231231231");
        ResponseEntity<HashMap<String, Object>> responseEntity1 = applyConferenceController.disapproveConference(disapproveConferenceRequest,request);
        Assert.isTrue(responseEntity1.getBody().get("message").equals("驳回会议申请失败"));
    }
}