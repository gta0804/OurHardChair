package fudan.se.lab2.controller.request;

import fudan.se.lab2.repository.UserRepositoryImps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author LBW
 */
@Controller
public class LoginRequest {
    private String username;
    private String password;

    private UserRepositoryImps userRepositoryImps;

    @Autowired
    public LoginRequest(UserRepositoryImps userRepositoryImps) {
        this.userRepositoryImps = userRepositoryImps;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
