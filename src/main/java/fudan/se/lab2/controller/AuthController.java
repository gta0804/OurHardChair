package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.RegisterRequest;
import fudan.se.lab2.domain.Authority;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.AuthorityRepository;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.AuthService;
import fudan.se.lab2.service.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.HashSet;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
            private AuthorityRepository authorityRepository;
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
        }
        HashSet<Authority> set = new HashSet<>();

        set.add(new Authority());
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword(), set);
            final Authentication authentication = authenticationManager.authenticate(userToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtil.generateToken(user);
            map.put("token", token);
            System.out.println("注册成功，发放token" + jwtTokenUtil.generateToken(user));
        map.put("message","success");
        map.put("username",user.getUsername());
        map.put("fullName",user.getFullName());
        map.put("email",user.getEmail());
        map.put("institution",user.getInstitution());
        map.put("country",user.getCountry());
        map.put("id",user.getId());
        return ResponseEntity.ok(map);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<HashMap<String,Object>> login(@RequestBody LoginRequest request) {
        System.out.println("接收到login请求");
        logger.debug("LoginForm: " + request.toString());
        HashMap<String,Object> map = new HashMap();
        UserDetails  userForBase = jwtUserDetailsService.loadUserByUsername(request.getUsername());
        if(null == userForBase){
            map.put("message","用户不存在");
            return ResponseEntity.ok(map);
        }
        if (!passwordEncoder.matches(request.getPassword(),userForBase.getPassword())){
            map.put("message","密码错误");
            return ResponseEntity.ok(map);
        }
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword(),
                userForBase.getAuthorities());
        final Authentication authentication = authenticationManager.authenticate(userToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.generateToken((User)userForBase);
        map.put("id",((User) userForBase).getId().intValue());
        map.put("message","success");
        map.put("token", token);
        map.put("username",userForBase.getUsername());
        map.put("fullName",((User) userForBase).getFullName());
        map.put("email",((User) userForBase).getEmail());
        map.put("institution",((User) userForBase).getInstitution());
        map.put("country",((User) userForBase).getCountry());
        return ResponseEntity.ok(map);
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

//    @CrossOrigin(origins = "*")
//    @RequestMapping("/modifyInformation")
//    public ResponseEntity<HashMap<String,Object>> modifyInformation(@RequestBody RegisterRequest request) {
//        logger.debug("ModifyInformationForm: " + request.toString());
//        HashMap<String,Object> map = new HashMap();
//        User user = authService.register(request);
//        if (null == user){
//            map.put("message","注册失败，已有该用户");
//            System.out.println("注册失败，已有该用户");
//            return ResponseEntity.ok(map);
//        }else {
//            String token = jwtTokenUtil.generateToken(user);
//            map.put("token",token);
//            System.out.println("注册成功，发放token" + jwtTokenUtil.generateToken(user));
//            map.put("message","success");
//            map.put("username",user.getUsername());
//            map.put("email",user.getEmail());
//            map.put("institution",user.getInstitution());
//            map.put("country",user.getCountry());
//            map.put("id",user.getId());
//            return ResponseEntity.ok(map);
//        }
//    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getUserDetails")
    public Object getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }
}



