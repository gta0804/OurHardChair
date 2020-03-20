package fudan.se.lab2.service;

import fudan.se.lab2.domain.Authority;
import fudan.se.lab2.exception.UsernameHasBeenRegisteredException;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.AuthorityRepository;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.controller.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author LBW
 */
@Service
public class AuthService {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    @Autowired
    public AuthService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    public User register(RegisterRequest request) {
        // TODO: Implement the function.
        if (null != userRepository.findByUsername(request.getUsername())){
            return null;
        }else{
            Set<Authority> set = new HashSet<>();
            Set<String> nameSet = request.getAuthorities();
            for (String s : nameSet) {
                set.add(new Authority(s));
            }
            User user = new User(request.getUsername(),request.getPassword(),request.getEmail(),request.getInstitution(),request.getCountry(),set);
            userRepository.save(user);
            return user;
        }
    }

    public String login(String username, String password) {
        // TODO: Implement the function.
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)){
                return "success";
            }
        }
        return "fail";
    }
}
