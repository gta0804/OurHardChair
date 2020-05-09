package fudan.se.lab2.controller;

import fudan.se.lab2.Lab2Application;
import fudan.se.lab2.controller.request.LoginRequest;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

@SpringBootTest(classes= Lab2Application.class)
public class ResultControllerTest {
    @Autowired
    private ResultController resultController;

    @Autowired
    private AuthController authController;

    private MockHttpServletRequest request;

    @Test
    void reviewReleaseResult(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        String message=(String)resultController.viewReviewResult((long)1,(long)1,request).getBody().get("message");
        Assert.isTrue(message.equals("请求成功"));
    }
}
