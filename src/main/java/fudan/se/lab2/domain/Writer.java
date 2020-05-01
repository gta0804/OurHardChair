package fudan.se.lab2.domain;

import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;

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

    private String writerName;
    private String email;
    private String institution;
    private String country;



    public Writer(){

    }
    public Writer(String writerName, String email, String institution, String country) {
        this.writerName = writerName;
        this.email = email;
        this.institution = institution;
        this.country = country;
    }
}
