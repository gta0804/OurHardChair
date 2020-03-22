package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ApplyMeetingRequest;
import fudan.se.lab2.domain.ApplyMeeting;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.ApplyMeetingRepository;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.AuthService;
import fudan.se.lab2.service.JwtUserDetailsService;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/register")
    public ResponseEntity<HashMap<String,Object>> register(@RequestBody RegisterRequest request) {
        logger.debug("RegistrationForm: " + request.toString());
        HashMap<String,Object> map = new HashMap();
        User user = authService.register(request);
        if (null == user){
            map.put("message","注册失败，已有该用户");
            System.out.println("注册失败，已有该用户");
            return ResponseEntity.ok(map);
        }else {
            String token = jwtTokenUtil.generateToken(user);
            map.put("token",token);
            System.out.println(jwtTokenUtil.generateToken(user));
            map.put("message","success");
            map.put("username",user.getUsername());
            map.put("email",user.getEmail());
            map.put("institution",user.getInstitution());
            map.put("country",user.getCountry());
            map.put("id",user.getId());
            return ResponseEntity.ok(map);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<HashMap<String,Object>> login(@RequestBody LoginRequest request) {
        System.out.println("接收到login请求");
        logger.debug("LoginForm: " + request.toString());
        HashMap<String,Object> map = new HashMap();
        UserDetails userForBase =  jwtUserDetailsService.loadUserByUsername(request.getUsername());
        System.out.println(userForBase);
        if(userForBase==null){
            System.out.println("用户不存在");
            map.put("message","用户不存在");
            return ResponseEntity.ok(map);
        }else {
            if (!passwordEncoder.matches(request.getPassword(),userForBase.getPassword())){
                map.put("message","密码错误");
                System.out.println("密码错误");
                return ResponseEntity.ok(map);
            }else {
                String token = jwtTokenUtil.generateToken((User)userForBase);
                System.out.println("登陆成功");
                System.out.println(token);
                map.put("id",((User) userForBase).getId());
                map.put("message","success");
                map.put("token", token);
                map.put("username",userForBase.getUsername());
                map.put("email",((User) userForBase).getEmail());
                map.put("institution",((User) userForBase).getInstitution());
                map.put("country",((User) userForBase).getCountry());
                return ResponseEntity.ok(map);
            }
        }
    }

    /*
        receive meeting application from frontend
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/ApplyConference")
    public ResponseEntity<HashMap<String,Object>> applyMeeting(@RequestHeader("Authorization") String rawToken,ApplyMeetingRequest request){
        String token= rawToken.substring(7);;
        Long id=userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();
        logger.debug("ApplyMeetingForm: " + request.toString());
        HashMap<String,Object> map = new HashMap();
        ApplyMeeting applyMeeting=authService.applyMeeting(request,id);
        if (null == applyMeeting){
            map.put("message","会议申请失败，已有该会议");
            return ResponseEntity.ok(map);
        }else {
            map.put("token",token);
            map.put("message","success");
            map.put("user id",applyMeeting.getApplicantId());
            map.put("abbreviation",applyMeeting.getAbbreviation());
            map.put("fullName",applyMeeting.getFullName());
            map.put("holdingTime",applyMeeting.getHoldingTime());
            map.put("holdingPlace",applyMeeting.getHoldingPlace());
            map.put("submissionDeadline",applyMeeting.getSubmissionDeadline());
            map.put("reviewReleaseDate",applyMeeting.getReviewReleaseDate());
            return ResponseEntity.ok(map);
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

    @CrossOrigin(origins = "*")
    @GetMapping("/getUserDetails")
    public Object getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }
}



