package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.RegisterRequest;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthControllerTest {

//    /**
//    * @Description: 测试登陆注册
//    * @Param:
//    * @return:
//    * @Author: Shen Zhengyu
//    * @Date: 2020/4/12
//    */
//    @Autowired
//    AuthController authController;
//
//    @Test
//    void register() {
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setFullName("admin");
//        registerRequest.setAuthorities(null);
//        registerRequest.setCountry("China");
//        registerRequest.setEmail("123@qq.com");
//        registerRequest.setInstitution("FD");
//        registerRequest.setPassword("123456");
//        registerRequest.setUsername("admin");
//        ResponseEntity<HashMap<String,Object>> responseEntity = authController.register(registerRequest);
//        Assert.isTrue(responseEntity.getBody().get("message").equals("注册失败，已有该用户"));
//        registerRequest.setUsername((new Date()).toString());
//        ResponseEntity<HashMap<String,Object>> responseEntity1 = authController.register(registerRequest);
//        Assert.isTrue(responseEntity1.getBody().get("email").equals("123@qq.com"));
//    }
//
//    @Test
//    void login() {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setUsername("admin");
//        loginRequest.setPassword("123");
//        Assert.isTrue(authController.login(loginRequest).getBody().get("message").equals("密码错误"));
//        loginRequest.setUsername((new Date()).toString()+ Math.random());
//        Assert.isTrue(authController.login(loginRequest).getBody().get("message").equals("用户不存在"));
//        loginRequest.setUsername("admin");
//        loginRequest.setPassword("password");
//        Assert.isTrue(authController.login(loginRequest).getBody().get("message").equals("success"));
//
//    }
//
//    @Test
//    void welcome() {
//        Map<String,String> map = (Map<String,String>)authController.welcome().getBody();
//
//        Assert.isTrue(map.get("message").equals("Welcome to 2020 Software Engineering Lab2. "));
//
//    }

}