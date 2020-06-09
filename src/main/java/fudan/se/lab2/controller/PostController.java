package fudan.se.lab2.controller;

import fudan.se.lab2.domain.Post;
import fudan.se.lab2.domain.Reply;
import fudan.se.lab2.repository.PostRepository;
import fudan.se.lab2.repository.ReplyRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @program: lab2
 * @description: 讨论相关功能
 * @author: Shen Zhengyu
 * @create: 2020-05-28 16:09
 **/
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RestController
public class PostController {
    Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
    * @Description: 查看所有与个人有关的帖子（自己有权限查看）
    * @Param: [httpServletRequest, userID]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */
    @CrossOrigin(origins = "*",allowCredentials = "true", allowedHeaders = "*")
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

    /**
    * @Description: 查看某一文章对应的帖子（bijection）
    * @Param: [httpServletRequest, articleID]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */
    @CrossOrigin(origins = "*",allowCredentials = "true", allowedHeaders = "*")
    @PostMapping("/browsePostOnArticle")
    public ResponseEntity<HashMap<String,Object>> browsePostOnArticle(HttpServletRequest httpServletRequest,@RequestParam(name = "articleID") Long articleID){
        logger.debug("browsePostOnArticle:" + articleID);
        HashMap<String,Object> map = new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        Post post = postService.browsePostsOnArticle(articleID);
        if(post == null){
            map.put("message","暂无帖子");
            map.put("token",token);
            map.put("post",null);
            return ResponseEntity.ok(map);

        }
        else{
            map.put("message","请求成功");
            map.put("token",token);
            map.put("post",post);
            return ResponseEntity.ok(map);
        }
    }

    /**
    * @Description: 针对某一个文章发起讨论
    * @Param: [httpServletRequest, articleID, ownerID, words]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */
    @CrossOrigin(origins = "*")
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


    /**
    * @Description: 针对某一个主题帖进行回帖
    * @Param: [httpServletRequest, postID, ownerID, words, floorNumber]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */
    @CrossOrigin(origins = "*")
    @PostMapping("/replyPost")
    public ResponseEntity<HashMap<String,Object>> replyPost(HttpServletRequest httpServletRequest,@RequestParam(name = "postID") Long postID,@RequestParam(name = "ownerID") Long ownerID,@RequestParam(name = "words") String words,@RequestParam(name = "floorNumber") Long floorNumber,HttpServletResponse response){
        logger.debug("replyPost");
        HashMap<String,Object> map = new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        Reply reply = postService.replyPost(postID,ownerID,words,floorNumber);
        String message = null == reply?"提交失败":"提交成功";
        map.put("message",message);
            map.put("token",token);
            map.put("reply",reply);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
            return ResponseEntity.ok(map);

    }

    /**
    * @Description: author对评审结果的质疑进行解释
    * @Param: [httpServletRequest, postID, authorID, words, articleID]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/28
    */

    @CrossOrigin(origins = "*",allowCredentials = "true", allowedHeaders = "*")
    @RequestMapping(value = "/submitRebuttal",method = RequestMethod.POST)
    public ResponseEntity<HashMap<String,Object>> submitRebuttal(HttpServletRequest httpServletRequest, @RequestParam(name = "authorID") Long authorID, @RequestParam(name = "words") String words, @RequestParam(name = "articleID") Long articleID){
        logger.debug(authorID + "submitRebuttal on " + articleID);
        HashMap<String,Object> map = new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);

        Reply reply = postService.submitRebuttal(articleID,words,authorID);
        String message = null == reply?"提交失败":"提交成功";
        map.put("message",message);
        map.put("token",token);
        map.put("reply",reply);
        return ResponseEntity.ok(map);

    }

}
