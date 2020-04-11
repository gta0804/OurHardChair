package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.MarkMessageRequest;
import fudan.se.lab2.domain.Message;
import fudan.se.lab2.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository=messageRepository;
    }

    /**
     *
     * @param userName
     * @return List<Message>
     * @description 得到所有未读的消息列表
     * @Author guo taian
     */
    public List<Message> getAllMessage(String userName){
        List<Message> messages=messageRepository.findAllByReceiverName(userName);
        if(messages==null){
            return null;
        }
        else{
            return messages;
        }
    }

    /**
     *
     * @param request
     * @description 标记消息为已读
     * @return
     */
    public boolean markRead(MarkMessageRequest request){
        Message messages=messageRepository.findBySenderNameAndReceiverNameAndRelatedConferenceNameAndMessageCategoryAndIsRead
                (
                        request.getSenderName(),
                        request.getReceiverName(),
                        request.getRelatedConferenceName(),
                        request.getMessageCategory(),
                                1);

        if(messages==null){
            return false;
        }
        else{
            messages.setIsRead(2);
            messageRepository.save(messages);
            return true;
        }
    }
}
