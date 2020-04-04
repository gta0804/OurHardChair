package fudan.se.lab2.controller.request;

//import jdk.nashorn.internal.objects.annotations.Constructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ApproveConferenceRequest {
    //The fullName of the conference
    private String fullName;

    @Autowired
    public ApproveConferenceRequest(){
    }

    public ApproveConferenceRequest(String fullName){
        this.fullName=fullName;
    }

    public String getFullName(){
        return this.fullName;
    }

    public void setFullName(String fullName){
        this.fullName=fullName;
    }

}

