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
    private Long conference_id;
    private String title;
    @Autowired
    public ShowContributionModificationRequest(){

    }
    public ShowContributionModificationRequest(Long conference_id, String title) {
        this.conference_id = conference_id;
        this.title = title;
    }

    public Long getConference_id() {
        return conference_id;
    }

    public void setConference_id(Long conference_id) {
        this.conference_id = conference_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
