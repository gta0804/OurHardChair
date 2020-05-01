package fudan.se.lab2.domain;

import javax.persistence.*;
import java.util.*;

@Entity
public class PCMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private Long  conferenceId;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<Topic> topics=new HashSet<>();
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

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public Long getId() {
        return id;
    }
}
