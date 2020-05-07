package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.*;
import fudan.se.lab2.domain.Article;
import fudan.se.lab2.repository.ArticleRepository;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.ContributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @program: lab2
 * @description: 接收投稿
 * @author: Shen Zhengyu
 * @create: 2020-04-08 09:11
 **/
@CrossOrigin
@RestController
public class ContributionController {

    Logger logger = LoggerFactory.getLogger(ContributionController.class);

    @Autowired
    ContributionService contributionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    /**
     * @Description: 接收投稿请求
     * @Param: [httpServletRequest, contributionRequest]
     * @return: org.springframework.http.ResponseEntity<java.util.HashMap < java.lang.String, java.lang.Object>>
     * @Author: Shen Zhengyu
     * @Date: 2020/4/8
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/contribute")
    public ResponseEntity<HashMap<String, Object>> contribute(HttpServletRequest httpServletRequest, @RequestBody ContributionRequest contributionRequest) {
        logger.debug("Try to submit article");
        HashMap<String, Object> map;
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        long conferenceID = contributionRequest.getConference_id();
        map = contributionService.contribute(contributionRequest);
        map.put("token",token);
        return ResponseEntity.ok(map);
    }

    /**
     * @Description: 接收上传的稿件文件
     * @Param: [httpServletRequest, contributionRequest]
     * @return: org.springframework.http.ResponseEntity<java.util.HashMap < java.lang.String, java.lang.Object>>
     * @Author: Shen Zhengyu
     * @Date: 2020/4/8
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/upload")
    public ResponseEntity<HashMap<String, Object>> upload(HttpServletRequest request, @RequestParam("file") MultipartFile file,@RequestParam("conferenceID") Long conferenceID)throws IOException {
        logger.debug("Try to upload...");
        HashMap<String, Object> map = new HashMap();
        String token = request.getHeader("Authorization").substring(7);
        map.put("token", token);
        if (null == file ||file.isEmpty()){
            map.put("message", "上传失败");
            return ResponseEntity.ok(map);
        }
        else {
            String fileName = file.getOriginalFilename();
            String path = null;
            String type = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.') + 1) : null;
//            if (type != null) {
//                if ("PDF".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    // 自定义的文件名称
                    // 设置存放图片文件的路径
                    //获取到了就传到对应参数的文件夹，获取不到就unknownConferenceID
                    StringBuilder sb = new StringBuilder("/workplace/upload/");

                    if (null == conferenceID) {
                        sb.append("unknownConferenceID/");
                    }else {
                        sb.append(conferenceID);
                        sb.append("/");
                    }
                    sb.append(fileName);
                    path = sb.toString();
                    mkdirAndFile(path);
                    File dest = new File(path);
                    file.transferTo(dest);
                    map.put("message", "上传成功");
                    map.put("存放路径", fileName);
                    return ResponseEntity.ok(map);
        }
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    public ResponseEntity<HashMap<String, Object>> update(HttpServletRequest request, @RequestParam("file") MultipartFile file,@RequestParam("conferenceID") Long conferenceID,@RequestParam("articleId") Long articleId)throws IOException {
        logger.debug("Try to update...");
        HashMap<String, Object> map = new HashMap();
        String token = request.getHeader("Authorization").substring(7);
        map.put("token", token);
        if (null == file ||file.isEmpty()){
            map.put("message", "上传失败");
            return ResponseEntity.ok(map);
        }
        else {
            String fileName = file.getOriginalFilename();
            String path = null;
            String type = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.') + 1) : null;
//            if (type != null) {
//                if ("PDF".equals(type.toUpperCase())) {
            // 项目在容器中实际发布运行的根路径
            String realPath = request.getSession().getServletContext().getRealPath("/");
            // 自定义的文件名称
            // 设置存放图片文件的路径
            //获取到了就传到对应参数的文件夹，获取不到就unknownConferenceID
            StringBuilder sb = new StringBuilder("/workplace/upload/");

            if (null == conferenceID) {
                sb.append("unknownConferenceID/");
            }else {
                sb.append(conferenceID);
                sb.append("/");
            }
            sb.append(fileName);
            path = sb.toString();
            mkdirAndFile(path);
            File dest = new File(path);
            file.transferTo(dest);
            Article article = articleRepository.findById(articleId).orElse(null);
            if (null != article) {
                article.setFilename(fileName);
                articleRepository.save(article);
                map.put("message", "重新上传成功");
                map.put("存放路径", fileName);
            }
            return ResponseEntity.ok(map);
        }
    }
    /**
    * @Description: 查看稿件的信息
    * @Param: [path]
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/4/26
    */
    @CrossOrigin(origins = "*")
    @PostMapping("/reviewArticle")
    public ResponseEntity<HashMap<String, Object>> reviewArticle(HttpServletRequest request, @RequestBody ReviewArticleRequest reviewArticleRequest) throws IOException {
        logger.debug("Try to review article...");
        String token = request.getHeader("Authorization").substring(7);
        HashMap<String,Object> message = contributionService.reviewArticle(reviewArticleRequest);
        message.put("token",token);
        return ResponseEntity.ok(message);
    }
    /**
    * @Description: 提交审稿信息
    * @Param: [request, showContributionModificationRequest]
    * @return: org.springframework.http.ResponseEntity<java.util.HashMap<java.lang.String,java.lang.Object>>
    * @Author: Shen Zhengyu
    * @Date: 2020/5/2
    */
    @CrossOrigin(origins = "*")
    @PostMapping("/submitReviewResult")
    public ResponseEntity<HashMap<String, Object>> submitReviewResult(HttpServletRequest request, @RequestBody SubmitReviewResultRequest submitReviewResultRequest){
        logger.debug("Try to submit review result...");
        HashMap<String, Object> answer = new HashMap<>();
        String token = request.getHeader("Authorization").substring(7);
        String message = contributionService.submitReviewResult(submitReviewResultRequest);
        answer.put("token",token);
        answer.put("message",message);
        return ResponseEntity.ok(answer);
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/showContributionModification")
    public ResponseEntity<HashMap<String, Object>> showContributionModification(HttpServletRequest request, @RequestBody ShowContributionModificationRequest showContributionModificationRequest) throws IOException {
        logger.debug("Try to show contribution modification...");
        String token = request.getHeader("Authorization").substring(7);
        HashMap<String,Object> message = contributionService.showContributionModification(showContributionModificationRequest);
        message.put("token",token);
        return ResponseEntity.ok(message);
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/modifyContribution")
    public ResponseEntity<HashMap<String, Object>> modifyContribution(HttpServletRequest request, @RequestBody ModifyContributionRequest modifyContributionRequest) throws IOException {
        logger.debug("Try to modify contribution...");
        String token = request.getHeader("Authorization").substring(7);
        HashMap<String,Object> message = contributionService.modifyContribution(modifyContributionRequest);
        message.put("token",token);
        return ResponseEntity.ok(message);
    }


        public void mkdirAndFile(String path) {
        //变量不需赋初始值，赋值后永远不会读取变量，在下一个变量读取之前，该值总是被另一个赋值覆盖
        File f;
        try {
            f = new File(path);
            if (!f.exists()) {
                boolean i = f.getParentFile().mkdirs();
                if (i) {
                    System.out.println("层级文件夹创建成功！");
                } else {
                    System.out.println("层级文件夹创建失败！");
                }
            }
            boolean b = f.createNewFile();
            if (b) {
                System.out.println("文件创建成功！");
            } else {
                System.out.println("文件创建失败！");
            }
        } catch (Exception e) {
            logger.error("error:" + e.getMessage() + e);
        }
    }
}

