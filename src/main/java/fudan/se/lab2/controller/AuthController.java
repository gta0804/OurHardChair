package fudan.se.lab2.controller;

import fudan.se.lab2.domain.User;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.AuthService;
import fudan.se.lab2.service.JwtUserDetailsService;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LBW
 */
@CrossOrigin()
@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.debug("RegistrationForm: " + request.toString());
        User user = authService.register(request);
        return ResponseEntity.ok(user);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<JSONObject> login(@RequestBody LoginRequest request) throws JSONException {
        System.out.println("来了");
        logger.debug("LoginForm: " + request.toString());
        JSONObject jsonObject=new JSONObject();
        UserDetails userForBase =  jwtUserDetailsService.loadUserByUsername(request.getUsername());
        System.out.println(userForBase);
        if(userForBase==null){
            jsonObject.put("message","登录失败,用户不存在");
            return ResponseEntity.ok(jsonObject);
        }else {
            if (!userForBase.getPassword().equals(request.getPassword())){
                jsonObject.put("message","登录失败,密码错误");
                return ResponseEntity.ok(jsonObject);
            }else {
                String token = jwtTokenUtil.generateToken((User)userForBase);
                jsonObject.put("token", token);
                jsonObject.put("user", (User)userForBase);
                return ResponseEntity.ok(jsonObject);
            }
        }
    }


    /**
     * This is a function to test your connectivity. (健康测试时，可能会用到它）.
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/welcome")
    public ResponseEntity<?> welcome() {
        Map<String, String> response = new HashMap<>();
        String message = "Welcome to 2020 Software Engineering Lab2. ";
        System.out.println("inlaile ");
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

}



