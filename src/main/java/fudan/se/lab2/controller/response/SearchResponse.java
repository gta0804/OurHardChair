package fudan.se.lab2.controller.response;

public class SearchResponse {
    private String fullName;
    private String username;
    private String email;
    private String institution;
    private String country;
    private Integer status;

    public SearchResponse(String fullName,String username,String email,String institution,String country,Integer status){
        this.username=username;
        this.fullName=fullName;
        this.email=email;
        this.institution=institution;
        this.country=country;
        this.status=status;
    }
}
