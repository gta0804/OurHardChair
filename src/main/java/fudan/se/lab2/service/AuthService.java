package fudan.se.lab2.service;

import fudan.se.lab2.domain.Authority;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.AuthorityRepository;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.controller.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;

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
        }
        System.out.println("注册成功！");
        String password = passwordEncoder.encode(request.getPassword());
        HashSet<Authority> set = new HashSet<>();
        Authority authority = getOrCreateAuthority(request.getUsername(),"user",authorityRepository);
        set.add(authority);
        User user = new User(request.getUsername(),password,request.getFullName(),request.getEmail(),request.getInstitution(),request.getCountry(),set);
        userRepository.save(user);
        return user;

    }
    private Authority getOrCreateAuthority(String username,String authorityText, AuthorityRepository authorityRepository) {
        Authority authority = authorityRepository.findByAuthority(authorityText);
        if (authority == null) {
            authority = new Authority(username,authorityText);
            authorityRepository.save(authority);
        }
        return authority;
    }


//    public User modifyInformation(@RequestBody ModifyInformationRequest request) {
//        //用户名重复的情况
//        if (null != userRepository.findByUsername(request.getUsername())){
//            System.out.println("要修改的用户名重复");
//            return null;
//        }else{
//            System.out.println("修改成功！");
//            String password = passwordEncoder.encode(request.getPassword());
//            HashSet<Authority> set = new HashSet<>();
//            Authority authority = authorityRepository.findByAuthority("user");
//            set.add(authority);
//        }
//        return null;
//    }

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

    public User getUserByUsername(String username) {
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }


}
