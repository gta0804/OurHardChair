package fudan.se.lab2;

import fudan.se.lab2.domain.Authority;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.AuthorityRepository;
import fudan.se.lab2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;

/**
 * Welcome to 2020 Software Engineering Lab2.
 * This is your first lab to write your own code and build a spring boot application.
 * Enjoy it :)
 *
 * @author LBW
 */
@SpringBootApplication
@EnableScheduling //开启定时任务
public class Lab2Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab2Application.class, args);
    }

    /**
     * This is a function to create some basic entities when the application starts.
     * Now we are using a In-Memory database, so you need it.
     * You can change it as you like.
     */
    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                // Create authorities if not exist.
                Authority adminAuthority = getOrCreateAuthority("admin","administrator", authorityRepository);
                Authority userAuthority = getOrCreateAuthority("superuser","user",authorityRepository);
                // Create an admin if not exists.
                if (userRepository.findByUsername("admin") == null) {
                    HashSet<Authority> set = new HashSet<>();
                    set.add(adminAuthority);
                    User admin = new User(
                            "admin",
                            passwordEncoder.encode("password"),
                            "libowen",
                            "libowen@fudan.edu.cn",
                            "Fudan University",
                            "China",
                            set
                    );
                    userRepository.save(admin);
                }
                if (userRepository.findByUsername("superuser") == null) {
                    HashSet<Authority> set = new HashSet<>();
                    set.add(userAuthority);
                    User superUser = new User(
                            "superuser",
                            passwordEncoder.encode("password"),
                            "superuser",
                            "superuser@fudan.edu.cn",
                            "Fudan University",
                            "China",
                            set
                    );
                    userRepository.save(superUser);
                }
            }

            private Authority getOrCreateAuthority(String username,String authorityText, AuthorityRepository authorityRepository) {
                Authority authority = authorityRepository.findByAuthority(authorityText);
                if (authority == null) {
                    authority = new Authority(username,authorityText);
                    authorityRepository.save(authority);
                }
                return authority;
            }
        };
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}

