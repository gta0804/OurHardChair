package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: lab2
 * @description: 查看要修改的稿件信息
 * @author: Shen Zhengyu
 * @create: 2020-04-26 17:54
 **/
@Controller
public class ShowContributionModificationRequest {
    private Long conferenceID;
    private String title;
    @Autowired
    public ShowContributionModificationRequest(){

    }
    public ShowContributionModificationRequest(Long conferenceID, String title) {
        this.conferenceID = conferenceID;
        this.title = title;
    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
