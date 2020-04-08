package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.ApprovePCNumberInvitationRequest;
import fudan.se.lab2.controller.request.DisapprovePCNumberInvitationRequest;
import fudan.se.lab2.controller.request.InvitePCNumberRequest;
import fudan.se.lab2.controller.request.SearchUserRequest;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.domain.Message;
import fudan.se.lab2.domain.PCMember;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.ConferenceRepository;
import fudan.se.lab2.repository.MessageRepository;
import fudan.se.lab2.repository.PCMemberRepository;
import fudan.se.lab2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PCMemberService {
    private PCMemberRepository pcMemberRepository;
    private ConferenceRepository conferenceRepository;
    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private final String REQUEST="PCNumberInvitationRequest";
    private final String RESPONSE="PCNumberInvitationResponse";

    @Autowired
    public PCMemberService(PCMemberRepository pcMemberRepository, ConferenceRepository conferenceRepository, UserRepository userRepository, MessageRepository messageRepository){
        this.pcMemberRepository=pcMemberRepository;
        this.conferenceRepository=conferenceRepository;
        this.userRepository=userRepository;
        this.messageRepository=messageRepository;
    }

    public String invitePCNumber(InvitePCNumberRequest request){
        List<String> users=request.getUsers();
        List<Message> messageList=new LinkedList<>();
        for(String userName:users){
            //数据库查不到该用户，数据库错误
            if(userRepository.findByUsername(userName)==null){
                return "错误，用户"+userName+"不存在";
            }
            User user=userRepository.findByUsername(userName);
            Conference conference=conferenceRepository.findByFullName(request.getFullName());
            PCMember pcMember=pcMemberRepository.findByUserIdAndConferenceId(user.getId(),conference.getId());
            //已经是PCNumber
            if(pcMember!=null){
                return user.getUsername()+"已经是PCNumber了";
            }
            Message message=messageRepository.
                    findAllByReceiverNameAndRelatedConferenceNameAndMessageCategoryAndIsRead
                            (
                                    userName,request.getFullName(),
                                    REQUEST,
                                    1);
            if(message!=null){
                return message.getReceiverName()+"还没有答复你的要求，请不要重复申请";
            }
            Long chairId=conferenceRepository.findByFullName(request.getFullName()).getChairId();
            User chair=userRepository.findById(chairId).orElse(null);
            Message messageToSave=new Message
                    (userName,chair.getUsername(),request.getFullName(),"用户"+userName+"发来一个加入"+request.getFullName()+"的申请","PCNumberInvitationRequest",1);
            messageList.add(messageToSave);
        }
        messageRepository.saveAll(messageList);
        return "success";
    }

    public boolean approvePCNumberInvitation(ApprovePCNumberInvitationRequest request){
        Message messageForRequest=messageRepository.
                findBySenderNameAndReceiverNameAndRelatedConferenceNameAndMessageCategoryAndIsRead
                        (
                                request.getReceiverName(),
                                request.getSenderName(),
                                request.getRelatedConferenceName(),
                                REQUEST,
                                1
                        );
        if(messageForRequest==null){
            return false;
        }
        Long userId=userRepository.findByUsername(request.getReceiverName()).getId();
        Long conferenceId=conferenceRepository.findByFullName(request.getRelatedConferenceName()).getId();
        if(userId==null||conferenceId==null){
            return false;
        }
        PCMember pcMember=new PCMember(userId,conferenceId);
        pcMemberRepository.save(pcMember);
        //删除请求
        messageRepository.delete(messageForRequest);
        //新建响应
        Message messageForResponse=new Message(
                request.getSenderName(),
                request.getReceiverName(),
                request.getRelatedConferenceName(),
                "你的PCNumber邀请已经被"+request.getSenderName()+"接受",
                RESPONSE,
                1

        );
        messageRepository.save(messageForResponse);
        return true;
    }

    public boolean disapprovePCNumberInvitation(DisapprovePCNumberInvitationRequest request){
        Message messageForRequest=messageRepository.
                findBySenderNameAndReceiverNameAndRelatedConferenceNameAndMessageCategoryAndIsRead
                        (
                                request.getReceiverName(),
                                request.getSenderName(),
                                request.getRelatedConferenceName(),
                                REQUEST,
                                1
                        );
        if(messageForRequest==null){
            return false;
        }
        //删除请求
        messageRepository.delete(messageForRequest);
        //新建响应
        Message messageForResponse=new Message(
                request.getSenderName(),
                request.getReceiverName(),
                request.getRelatedConferenceName(),
                "你的PCNumber邀请已经被"+request.getSenderName()+"拒绝",
                RESPONSE,
                1

        );
        messageRepository.save(messageForResponse);
        return true;
    }




    public List<User> search(SearchUserRequest request){
        List<User> users=userRepository.findAllByFullNameContaining(request.getSearchKey());
        return users;
    }

}
