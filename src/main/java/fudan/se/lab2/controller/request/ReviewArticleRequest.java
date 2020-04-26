package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @program: lab2
 * @description: 发来的查看评审文章的请求
 * @author: Shen Zhengyu
 * @create: 2020-04-26 12:17
 **/
@Controller
public class ReviewArticleRequest {
    private Long conferenceID;
    private String title;

    @Autowired
    public ReviewArticleRequest(){

    }

    public ReviewArticleRequest(Long conferenceID, String title) {
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
