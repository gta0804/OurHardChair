package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.ApprovePCMemberInvitationRequest;
import fudan.se.lab2.controller.request.DisapprovePCMemberInvitationRequest;
import fudan.se.lab2.controller.request.InvitePCMemberRequest;
import fudan.se.lab2.controller.request.SearchUserRequest;
import fudan.se.lab2.controller.response.SearchResponse;
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
    private final String REQUEST="PCMemberInvitationRequest";
    private final String RESPONSE="PCMemberInvitationResponse";

    @Autowired
    public PCMemberService(PCMemberRepository pcMemberRepository, ConferenceRepository conferenceRepository, UserRepository userRepository, MessageRepository messageRepository){
        this.pcMemberRepository=pcMemberRepository;
        this.conferenceRepository=conferenceRepository;
        this.userRepository=userRepository;
        this.messageRepository=messageRepository;
    }

    public String invitePCNumber(InvitePCMemberRequest request){
        List<String> users=request.getUsers();
        List<Message> messageList=new LinkedList<>();
        for(String userName:users){
            //数据库查不到该用户，数据库错误
            if(userRepository.findByUsername(userName)==null){
                return "错误，用户"+userName+"不存在";
            }
            Long chairId=conferenceRepository.findByFullName(request.getFullName()).getChairId();
            User chair=userRepository.findById(chairId).orElse(null);
            Message messageToSave=new Message
                    (chair.getUsername(),userName,request.getFullName(),"用户"+chair.getUsername()+"发来一个加入"+request.getFullName()+"的申请",REQUEST,1);
            messageList.add(messageToSave);
        }
        messageRepository.saveAll(messageList);
        return "success";
    }

    public boolean approvePCNumberInvitation(ApprovePCMemberInvitationRequest request){
        Message messageForRequest=messageRepository.
                findBySenderNameAndReceiverNameAndRelatedConferenceNameAndMessageCategoryAndIsRead
                        (
                                //这里SenderName指向被邀请人，ReceiverName指向会议主持者
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

    public boolean disapprovePCNumberInvitation(DisapprovePCMemberInvitationRequest request){
        Message messageForRequest=messageRepository.
                findBySenderNameAndReceiverNameAndRelatedConferenceNameAndMessageCategoryAndIsRead
                        (
                                //这里SenderName指向被邀请人，ReceiverName指向会议主持者
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




    public List<SearchResponse> search(SearchUserRequest request){
        List<User> users=userRepository.findAllByFullNameContaining(request.getSearch_key());
        System.out.println("length: "+users.size());
        List<SearchResponse> responses=new ArrayList<>();
        for(User user:users){
            if(isAdminOrUser(request.getFull_Name(),user.getUsername())){
                continue;
            }
            else if(isPCMember(request.getFull_Name(),user.getUsername())){
                responses.add(new SearchResponse(
                        user.getFullName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getInstitution(),
                        user.getCountry(),
                        2));
            }
            else if(!isHandled(request.getFull_Name(),user.getUsername())){
                responses.add(new SearchResponse(
                        user.getFullName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getInstitution(),
                        user.getCountry(),
                        3));
            }
            else{
                responses.add(new SearchResponse(
                        user.getFullName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getInstitution(),
                        user.getCountry(),
                        1));
            }
        }
        return responses;
    }

    public boolean isAdminOrUser(String conferenceName,String username) {
        if (username.equals("admin")) {
            return true;
        }
        Conference conference = conferenceRepository.findByFullName(conferenceName);
        User user = userRepository.findById(conference.getChairId()).orElse(null);

        if (username.equals(user.getUsername())) {
            return true;
        }
        return false;
    }

    public boolean isPCMember(String conferenceName,String username){
        User user=userRepository.findByUsername(username);
        Conference conference=conferenceRepository.findByFullName(conferenceName);
        PCMember pcMember=pcMemberRepository.findByUserIdAndConferenceId(user.getId(),conference.getId());
        if(pcMember!=null){
            return true;
        }
        return false;
    }

    public boolean isHandled(String conferenceName,String username){
        Message message=messageRepository.
                findAllByReceiverNameAndRelatedConferenceNameAndMessageCategoryAndIsRead
                        (
                                username,
                                conferenceName,
                                REQUEST,
                                1);
        if(message==null){
            return true;
        }
        return false;
    }

}
