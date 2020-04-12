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

    @Autowired
    UserRepository userRepository;

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
        HashMap<String, Object> map = new HashMap();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
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
        logger.debug("Try to upload");
        HashMap<String, Object> map = new HashMap();
        String token = request.getHeader("Authorization").substring(7);
        map.put("token", token);
        System.out.println("进入了uploadfa那个发");
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String path = null;
            String type = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
            if (type != null) {
                if ("PDF".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    // 自定义的文件名称
                    // 设置存放图片文件的路径
                    path = "/workplace/classwork" + fileName;
                    System.out.println(path);
                    mkdirAndFile(path);
                    File dest = new File(path);
                    file.transferTo(dest);
                    map.put("message", "上传成功");
                    map.put("存放路径", fileName);
                    return ResponseEntity.ok(map);
                } else {
                    System.out.println("不是pdf");
                    map.put("message", "上传失败");
                }
            } else {
                System.out.println("type是null");
                map.put("message", "上传失败");
            }
        } else {
            System.out.println("没收到file");
            map.put("message", "上传失败");
        }
        return ResponseEntity.ok(map);
    }

    public void mkdirs(String path) {
        //变量不需赋初始值，赋值后永远不会读取变量，在下一个变量读取之前，该值总是被另一个赋值覆盖
        File f;
        try {
            f = new File(path);
            if (!f.exists()) {
                boolean i = f.mkdirs();
                if (i) {
                    System.out.println("成功");
                } else {
                    System.out.println("层级文件夹创建失败！");
                }
            }
        } catch (Exception e) {
            logger.error("error:" + e.getMessage() + e);
        }
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

