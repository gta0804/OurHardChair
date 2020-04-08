package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DisapproveConferenceRequest {
    private String fullName;
    @Autowired
    public DisapproveConferenceRequest(){

    }

    public DisapproveConferenceRequest(String fullName){
        this.fullName=fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
