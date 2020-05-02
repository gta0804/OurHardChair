package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-05-02 14:36
 **/
@Controller
public class UploadRequest {
    private MultipartFile file;
    private Long conferenceID;
    @Autowired
    public UploadRequest() {
    }

    public UploadRequest(MultipartFile file, Long conferenceID) {
        this.file = file;
        this.conferenceID = conferenceID;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Long getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(Long conferenceID) {
        this.conferenceID = conferenceID;
    }
}
