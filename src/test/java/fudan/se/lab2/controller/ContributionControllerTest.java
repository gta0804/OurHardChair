package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ContributionRequest;
import fudan.se.lab2.controller.request.ShowSubmissionRequest;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;
import java.util.HashMap;

@SpringBootTest
class ContributionControllerTest {

    @Autowired
    ContributionController contributionController;

    @Autowired
    MyRelatedConferenceController myRelatedConferenceController;
    private MockHttpServletRequest request;

    String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTU4NjY4MDYzMCwiZXhwIjoxNTg2Njk4NjMwfQ.laMZ1U8mDn53ig9AG4sw23XKMasthIqCd0YDnfV9K9GTICGprAdthhhYj0RZqmMjb09iGd5-OsznQRudUJBmKw";

    /**
    * @Description: 测试投稿
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/12
    */
    @Test
    void contribute() {
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization", token);
        ContributionRequest contributionRequest = new ContributionRequest();
        contributionRequest.setArticleAbstract("123");
        contributionRequest.setContributorID((long)2);
        contributionRequest.setConference_id((long)1);
        contributionRequest.setFilename("1.pdf");
        contributionRequest.setTitle((new Date()).toString() + Math.random());
        Assert.isTrue(contributionController.contribute(request,contributionRequest).getBody().get("message").equals("投稿成功"));
        Assert.isTrue(contributionController.contribute(request,contributionRequest).getBody().get("message").equals("重复投稿（标题名重复）"));
    }

    @Test
    void upload() {
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization", token);
//        try {
//            Assert.isTrue(contributionController.upload(request,null).getBody().get("message").equals("上传失败"));
//        } catch (IOException e) {
//
//        }
    }

    @Test
    void showSubmission(){
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization", token);
        ShowSubmissionRequest showSubmissionRequest=new ShowSubmissionRequest();
        showSubmissionRequest.setConference_id((long)2);
        ResponseEntity<HashMap<String,Object>> responseEntity=myRelatedConferenceController.showMySubmission(showSubmissionRequest,request);
        Assert.isTrue(responseEntity.getBody().get("message").equals("success"));
    }

}