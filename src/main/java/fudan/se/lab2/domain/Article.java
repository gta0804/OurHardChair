package fudan.se.lab2.domain;

import fudan.se.lab2.controller.request.componment.WriterRequest;

import javax.persistence.*;
import java.util.*;

/**
 * @program: lab2
 * @description: ConferenceArticle
 * @author: Shen Zhengyu
 * @create: 2020-04-08 08:57
 **/
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long conferenceID;

    private Long contributorID;

    private String filename;

    private String title;

    private String articleAbstract;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
    private List<Writer> writers = new ArrayList<>();

    @ManyToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Topic> topics=new HashSet<>();

    @ManyToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Set<PCMember> pcMembers=new HashSet<>();
    //0暂时无法发布 1已经可以发布 2主席已经发布
    private Long status;

    private Integer howManyPeopleHaveReviewed;
    public Article() {
    }

    /**
    * @Description:
    * @Param: [conferenceID, AuthorID, filename, title, articleAbstract]
    * @return:
    * @Author: Shen Zhengyu
    * @Date: 2020/4/8
    */
    public Article(Long conferenceID,Long contributorID,String filename,String title,String articleAbstract,List<Writer> writers)
    {
        this.conferenceID = conferenceID;
        this.contributorID = contributorID;
        this.filename = filename;
        this.title = title;
        this.articleAbstract = articleAbstract;
        this.status = (long)0;
        this.writers = writers;
        this.howManyPeopleHaveReviewed = 0;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
    }

    public Long getContributorID() {
        return contributorID;
    }

    public void setContributorID(Long authorID) {
        this.contributorID = authorID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    public List<Writer> getWriters() {
        return writers;
    }

    public void setWriters(List<Writer> writers) {
        this.writers = writers;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public Set<PCMember> getPcMembers() {
        return pcMembers;
    }

    public void setPcMembers(Set<PCMember> pcMembers) {
        this.pcMembers = pcMembers;
    }

    public Integer getHowManyPeopleHaveReviewed() {
        return howManyPeopleHaveReviewed;
    }

    public void setHowManyPeopleHaveReviewed(Integer howManyPeopleHaveReviewed) {
        this.howManyPeopleHaveReviewed = howManyPeopleHaveReviewed;
    }
}
