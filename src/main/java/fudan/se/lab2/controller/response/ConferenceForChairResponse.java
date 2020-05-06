package fudan.se.lab2.controller.response;

import fudan.se.lab2.domain.Conference;

import java.util.List;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-06 13:44
 **/
public class ConferenceForChairResponse {
    private Conference conference;
    private Integer canRelease;

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Integer getCanRelease() {
        return canRelease;
    }

    public void setCanRelease(Integer canRelease) {
        this.canRelease = canRelease;
    }
}
