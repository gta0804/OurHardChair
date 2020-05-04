package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OpenManuscriptReviewRequest {
    private Long conferenceId;
    private Integer allocationStrategy;

    @Autowired
    public OpenManuscriptReviewRequest(){

    }

    public Long getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Long conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Integer getAllocationStrategy() {
        return allocationStrategy;
    }

    public void setAllocationStrategy(Integer allocationStrategy) {
        this.allocationStrategy = allocationStrategy;
    }
}
