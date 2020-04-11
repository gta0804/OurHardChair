package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.*;

/**
 * @description : 接收PCNumberRequest请求
 */
@Controller
public class InvitePCMemberRequest {
    private String full_name;
    private List<String> users;
    @Autowired
    public InvitePCMemberRequest(){

    }


    public InvitePCMemberRequest(List<String> users, String full_name){
        this.full_name=full_name;
        this.users=users;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String fullName) {
        this.full_name = fullName;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
