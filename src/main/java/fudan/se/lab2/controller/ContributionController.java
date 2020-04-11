package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.ContributionRequest;
import fudan.se.lab2.controller.request.RegisterRequest;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.security.jwt.JwtTokenUtil;
import fudan.se.lab2.service.ContributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    UserRepository userRepository;

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
        HashMap<String, Object> map = new HashMap();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        Long id = userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();
        String status = contributionService.saveContribution(contributionRequest);
        if (status.equals("duplicate contribution")) {
            map.put("message", "重复投稿（标题名重复）");
            map.put("token", token);
        } else if (status.equals("successful contribution")) {
            map.put("message", "投稿成功");
            map.put("token", token);
        } else {
            map.put("message", "未知错误");
            map.put("token", token);
        }
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
    public ResponseEntity<HashMap<String, Object>> upload(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String user = request.getParameter("user");
        logger.debug("Try to upload");
        HashMap<String, Object> map = new HashMap();
        String token = request.getHeader("Authorization").substring(7);
        map.put("token", token);
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String path = null;
            String type = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            if (type != null) {
                if ("DOCX".equals(type.toUpperCase()) || "DOC".equals(type.toUpperCase()) || "PDF".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    // 自定义的文件名称
                    String trueFileName = user + "_" + fileName;

                    // 设置存放图片文件的路径
                    path = "/workplace/classwork/" + trueFileName;
                    File dest = new File(path);
                    //判断文件父目录是否存在
                    if (!dest.getParentFile().exists()) {
                        dest.getParentFile().mkdir();
                    }

                    file.transferTo(dest);
                    map.put("message","上传成功");
                    map.put("存放路径",trueFileName);
                    return ResponseEntity.ok(map);
                } else {
                    map.put("message","上传失败");
                }
            } else {
                map.put("message","上传失败");
            }
        } else {
            map.put("message","上传失败");
        }
        return ResponseEntity.ok(map);

    }
    }

