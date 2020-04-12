package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.HandlePCMemberInvitationRequest;
import fudan.se.lab2.controller.request.InvitePCMemberRequest;
import fudan.se.lab2.controller.request.SearchUserRequest;
import fudan.se.lab2.controller.response.SearchResponse;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.PCMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin
@Controller
public class PCMemberController {
    private Logger logger = LoggerFactory.getLogger(PCMemberController.class);
    private PCMemberService pcMemberService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    public PCMemberController(PCMemberService pcMemberService){
        this.pcMemberService=pcMemberService;
    }

    @CrossOrigin("*")
    @PostMapping("/invitePCMember")
    public ResponseEntity<HashMap<String,Object>> invitePCMember(@RequestBody InvitePCMemberRequest request, HttpServletRequest httpServletRequest){
        logger.debug("inviting PCMember "+request.toString());
        HashMap<String,Object> map=new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String result=pcMemberService.invitePCNumber(request);
        if(result.equals("success")){
            map.put("message",result);
            map.put("token",token);
            return ResponseEntity.ok(map);
        }
        else{
            map.put("message","error");
            return ResponseEntity.ok(map);
        }
    }

    @CrossOrigin("*")
    @PostMapping("/approvePCMemberInvitation")
    public ResponseEntity<HashMap<String,Object>> approvePCMember(@RequestBody HandlePCMemberInvitationRequest request, HttpServletRequest httpServletRequest){
        logger.debug("approve PCMember "+request.toString());
        HashMap<String,Object> map=new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        boolean result=pcMemberService.approvePCNumberInvitation(request);
        if(!result){
            map.put("message","error");
            return ResponseEntity.ok(map);
        }
        else{
            map.put("message","success");
            map.put("token",token);
            return ResponseEntity.ok(map);
        }
    }


    @CrossOrigin("*")
    @PostMapping("/disapprovePCMemberInvitation")
    public ResponseEntity<HashMap<String,Object>> disapprovePCMember(@RequestBody HandlePCMemberInvitationRequest request, HttpServletRequest httpServletRequest){
        logger.debug("disapprove PCMember "+request.toString());
        HashMap<String,Object> map=new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        boolean result=pcMemberService.disapprovePCNumberInvitation(request);
        if(!result){
            map.put("message","error");
            return ResponseEntity.ok(map);
        }
        else{
            map.put("message","success");
            map.put("token",token);
            return ResponseEntity.ok(map);
        }
    }

    @CrossOrigin("*")
    @PostMapping("/search")
    public ResponseEntity<HashMap<String,Object>>search(@RequestBody SearchUserRequest request,HttpServletRequest httpServletRequest){
        logger.debug("searching people"+request.toString());
        System.out.println("search key: "+request.getSearch_key());
        System.out.println("fullName: "+request.getFull_Name());
        HashMap<String,Object> map=new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        List<SearchResponse> responses=pcMemberService.search(request);
        if(responses==null){
            map.put("message","error");
            return ResponseEntity.ok(map);
        }
        else{
            map.put("message","success");
            map.put("token",token);
            map.put("users",responses);
            return ResponseEntity.ok(map);
        }

    }
}