package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ApprovePCNumberInvitationRequest;
import fudan.se.lab2.controller.request.DisapprovePCNumberInvitationRequest;
import fudan.se.lab2.controller.request.InvitePCNumberRequest;
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
    public ResponseEntity<HashMap<String,Object>> invitePCMember(@RequestBody InvitePCNumberRequest request, HttpServletRequest httpServletRequest){
        logger.debug("inviting PCMember "+request.toString());
        HashMap<String,Object> map=new HashMap();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String result=pcMemberService.invitePCNumber(request);
        if(result.equals("success")){
            map.put("message",result);
            return ResponseEntity.ok(map);
        }
        else{
            map.put("message","success");
            map.put("token",token);
            return ResponseEntity.ok(map);
        }
    }

    @CrossOrigin("*")
    @PostMapping("/approvePCMemberInvitation")
    public ResponseEntity<HashMap<String,Object>> approvePCMember(@RequestBody ApprovePCNumberInvitationRequest request, HttpServletRequest httpServletRequest){
        logger.debug("inviting PCMember "+request.toString());
        HashMap<String,Object> map=new HashMap();
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
    public ResponseEntity<HashMap<String,Object>> disapprovePCMember(@RequestBody DisapprovePCNumberInvitationRequest request, HttpServletRequest httpServletRequest){
        logger.debug("inviting PCMember "+request.toString());
        HashMap<String,Object> map=new HashMap();
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
}