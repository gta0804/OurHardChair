package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ApplyMeetingRequest;
import fudan.se.lab2.controller.request.InvitePCMemberRequest;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.domain.Post;
import fudan.se.lab2.domain.Reply;
import fudan.se.lab2.repository.PostRepository;
import fudan.se.lab2.repository.ReplyRepository;
import fudan.se.lab2.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @program: lab2
 * @description: 讨论相关功能
 * @author: Shen Zhengyu
 * @create: 2020-05-28 16:09
 **/
@CrossOrigin
@Controller
public class PostController {
    Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private PostService postService;

    @CrossOrigin("*")
    @PostMapping("/browseAllPosts")
    public ResponseEntity<HashMap<String,Object>> browseAllPosts(HttpServletRequest httpServletRequest,@RequestParam(name = "userID") Long userID){
        logger.debug("browseAllPosts");
        HashMap<String,Object> map=new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        ArrayList<Post> posts = (ArrayList<Post>) postService.browseAllPosts(userID);
        if(posts == null){
            map.put("message","暂无帖子");
            map.put("token",token);
            map.put("postList",null);
            return ResponseEntity.ok(map);

        }
        else{
            map.put("message","请求成功");
            map.put("token",token);
            map.put("postList",posts);
            return ResponseEntity.ok(map);
        }
    }

    @CrossOrigin("*")
    @PostMapping("/browsePostOnArticle")
    public ResponseEntity<HashMap<String,Object>> browsePostOnArticle(HttpServletRequest httpServletRequest,@RequestParam(name = "articleID") Long articleID){
        logger.debug("browsePostOnArticle:" + articleID);
        HashMap<String,Object> map = new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        Post post = postService.browsePostsOnArticle(articleID);
        if(post == null){
            map.put("message","暂无帖子");
            map.put("token",token);
            map.put("postList",null);
            return ResponseEntity.ok(map);

        }
        else{
            map.put("message","请求成功");
            map.put("token",token);
            map.put("postList",post);
            return ResponseEntity.ok(map);
        }
    }

    @CrossOrigin("*")
    @PostMapping("/postOnArticle")
    public ResponseEntity<HashMap<String,Object>> postOnArticle(HttpServletRequest httpServletRequest,@RequestParam(name = "articleID") Long articleID,@RequestParam(name = "ownerID") Long ownerID,@RequestParam(name = "words") String words){
        logger.debug("Post:" +ownerID + "on" + articleID);
        HashMap<String,Object> map = new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        Long postID = postService.postOnArticle(articleID,ownerID,words);
        if(postID == (long)(-1)){
            map.put("message","发送失败");
            map.put("token",token);
            map.put("postID",postID);
            return ResponseEntity.ok(map);

        }else if (postID == (long)(-255)){
            map.put("message","重复发送");
            map.put("token",token);
            map.put("postID",postID);
            return ResponseEntity.ok(map);
        }
        else{
            map.put("message","发送成功");
            map.put("token",token);
            map.put("postID",postID);
            return ResponseEntity.ok(map);
        }
    }

    @CrossOrigin("*")
    @PostMapping("/replyPost")
    public ResponseEntity<HashMap<String,Object>> replyPost(HttpServletRequest httpServletRequest,@RequestParam(name = "postID") Long postID,@RequestParam(name = "ownerID") Long ownerID,@RequestParam(name = "words") String words,@RequestParam(name = "floorNumber") Long floorNumber){
        logger.debug("replyPost");
        HashMap<String,Object> map = new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        Reply reply = postService.replyPost(postID,ownerID,words,floorNumber);
            map.put("message","回帖成功");
            map.put("token",token);
            map.put("reply",reply);
            return ResponseEntity.ok(map);

    }

}
