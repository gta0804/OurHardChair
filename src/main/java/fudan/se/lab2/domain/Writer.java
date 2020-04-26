package fudan.se.lab2.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @program: lab2
 * @description: 投稿文章的作者
 * @author: Shen Zhengyu
 * @create: 2020-04-26 20:16
 **/
@Entity
public class Writer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;
    private String email;
    private String institution;
    private String country;

    public Writer(){

    }
    public Writer(String fullName, String email, String institution, String country) {
        this.fullName = fullName;
        this.email = email;
        this.institution = institution;
        this.country = country;
    }
}
