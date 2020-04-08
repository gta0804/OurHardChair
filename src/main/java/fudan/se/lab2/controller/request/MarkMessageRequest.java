package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/*
将消息标记为已读
 */
@Controller
public class MarkMessageRequest {
    private String senderName;
    private String receiverName;
    private String relatedConferenceName;
    private String messageCategory;

    @Autowired
    public MarkMessageRequest(){

    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }


    public String getMessageCategory() {
        return messageCategory;
    }


    public void setMessageCategory(String messageCategory) {
        this.messageCategory = messageCategory;
    }

    public String getRelatedConferenceName() {
        return relatedConferenceName;
    }

    public void setRelatedConferenceName(String relatedConferenceName) {
        this.relatedConferenceName = relatedConferenceName;
    }
}
