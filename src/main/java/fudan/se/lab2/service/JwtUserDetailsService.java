package fudan.se.lab2.service;

import fudan.se.lab2.domain.Authority;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.AuthorityRepository;
import fudan.se.lab2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * @author LBW
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 根据用户名获取认证用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: Implement the function.

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        } else {
            //封装用户信息和角色信息到SecurityContextHolder全局缓存中

            List<Authority> grantedAuthorities = new ArrayList<Authority>();
            //   获得一个迭代子
            for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
                grantedAuthorities.add((Authority) grantedAuthority);
            }
            Set<Authority> set = new HashSet<>(grantedAuthorities);
            /*
             * 创建一个用于认证的用户对象并返回，包括：用户名，密码，角色
             */
            return new User(user.getUsername(), user.getPassword(), user.getEmail(),user.getInstitution(),user.getCountry(),set);
        }
    }
}
