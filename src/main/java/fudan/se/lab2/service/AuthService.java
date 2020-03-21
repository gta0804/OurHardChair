package fudan.se.lab2.service;

import fudan.se.lab2.domain.Authority;
import fudan.se.lab2.exception.UsernameHasBeenRegisteredException;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.AuthorityRepository;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.controller.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import fudan.se.lab2.security.SecurityConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author LBW
 */
@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public AuthService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }


    public User register(@RequestBody RegisterRequest request) {
        // TODO: Implement the function.
        //用户名重复的情况
        if (null != userRepository.findByUsername(request.getUsername())){
            System.out.println("用户名重复");
            return null;
        }else{
            System.out.println("注册成功！");
            String password = passwordEncoder.encode(request.getPassword());
            User user = new User(request.getUsername(),password,request.getEmail(),request.getInstitution(),request.getCountry(),null);
            userRepository.save(user);
            System.out.println("加入新用户" +user.getUsername() + "成功！");
            return user;
        }
    }

        public String login(String username, String password) {
        // TODO: Implement the function.
        Iterable<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getUsername().equals(username) && passwordEncoder.matches(password,user.getPassword())){
                return "success";
            }
        }
            return "fail";


    }
}
