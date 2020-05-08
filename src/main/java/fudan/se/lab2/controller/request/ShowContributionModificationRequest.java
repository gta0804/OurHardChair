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
    @Autowired
    public ShowContributionModificationRequest(){

    }

    public Long getConference_id() {
        return conference_id;
    }

    public void setConference_id(Long conference_id) {
        this.conference_id = conference_id;
    }

}
