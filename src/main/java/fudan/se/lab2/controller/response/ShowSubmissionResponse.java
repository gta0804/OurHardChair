package fudan.se.lab2.controller.response;

public class ShowSubmissionResponse {
    private String conferenceName;
    private String filename;
    private String title;
    private String articleAbstract;
    private Integer status;

    public ShowSubmissionResponse(String conferenceName,String filename,String title,String articleAbstract,Integer status){
        this.conferenceName=conferenceName;
        this.filename=filename;
        this.title=title;
        this.articleAbstract=articleAbstract;
        this.status=status;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
