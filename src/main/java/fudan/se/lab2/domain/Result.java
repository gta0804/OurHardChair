package fudan.se.lab2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: lab2
 * @description: 记录审稿的信息
 * @author: Shen Zhengyu
 * @create: 2020-05-02 15:49
 **/
@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long conferenceID;
    private String title;
    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Evaluation> evaluations;

    public Result(){}
    public Result(Long conferenceID, String title, Set<Evaluation> evaluations) {
        this.conferenceID = conferenceID;
        this.title = title;
        this.evaluations = evaluations;
    }

    public Set<Evaluation> getEvaluations() {
        return evaluations;
    }
}
