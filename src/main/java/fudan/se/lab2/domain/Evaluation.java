package fudan.se.lab2.domain;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-02 15:52
 **/
@Entity
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long PCMemberID;
    private Integer score;
    private String comment;
    private Integer confidence;
    public Evaluation(){}
    public Evaluation(Long PCMemberID, Integer score, String comment, Integer confidence) {
        this.PCMemberID = PCMemberID;
        this.score = score;
        this.comment = comment;
        this.confidence = confidence;
    }
}
