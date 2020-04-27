package fudan.se.lab2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-04-26 20:30
 **/
@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String topic;
    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Conference> conference=new HashSet<>();

    public Topic(){}
    
    public Topic(String topics) {
        this.topic = topics;
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topics) {
        this.topic = topics;
    }

    public Set<Conference> getConference() {
        return conference;
    }

    public void setConference(Set<Conference> conference) {
        this.conference = conference;
    }
}
