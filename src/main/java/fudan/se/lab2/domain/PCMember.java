package fudan.se.lab2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PCMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private Long  conferenceId;

    public PCMember() {
    }

    public PCMember(Long userId, Long conferenceId){
        this.userId=userId;
        this.conferenceId=conferenceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getConferenceId() {
        return this.conferenceId;
    }

    public void setMeetingId(Long conferenceId) {
        this.conferenceId = conferenceId;
    }

}
