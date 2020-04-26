package fudan.se.lab2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Set;

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
    //会议的全称
    private String fullName;
    //所有的topics,用"||"分割
    private String topics;

    public Topic(){}
    public Topic(Long id, String fullName, String topics) {
        this.id = id;
        this.fullName = fullName;
        this.topics = topics;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }
}
