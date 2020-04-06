## 后端更新日志
### _2020.4.5 更新 沈征宇_

1. 会议审核前后端对接，发放token
2. 所有会议显示的前后端对接
3. 实现了查看所有我已申请的会议
   添加MyRelatedConferenceController，处理所有与我有关会议的请求请发到这个路由

### _2020.4.4 更新 沈征宇_

1. 添加了单元测试
2. 添加了消息数据表和消息实体

### _2020.4.4 更新 郭泰安_

1. 实现了会议审核的数据层和业务层。
2. 重构了ApplyMeeting数据表，添加了两个新的数据列，删除了UserRole,新建PCMember 和Author。如果之前ApplyMeeting里面存了旧的数据，请到<localhost：8080/h2-console>重构该数据表，并删除UserRole数据表。
3. 优化了service层和controller层的代码结构。

### _2020.4.3 更新 郭泰安_

1. 添加fullName
2. 将数据库改为h2





### _2020.3.23更新 沈征宇_
1. 适配前后端数据传输名称问题，成功修正会议申请

### _2020.3.22更新 郭泰安_
1. 修改了数据库的数据结构，请兄弟们使用  

    drop database project;
    create database project;  
    重构数据库  
2. 修复了注册时单位为null的bug。
3. 修复了不同数据表主键自增耦合的问题。
4. 新建了UserRole实体类，用于存储会议内用户的角色信息。
   
### _2020.3.22 更新 沈征宇_
1 注册和登录后返回用户ID到前端

    规范User表中的Authorities中的字符串内容：只能是"user"或"administrator"


2 已经测试的条目
   1. 登录，注册
   2. 可以检测注册时的用户名重复

3 更新了数据库默认生成方式

### _2020.3.21 更新 郭泰安_

1. 实现了会议申请的数据层和业务层

2. token有部分未实现，劳请兄弟们帮我实现一下。

    **在ApplyMeeting中取token已实现——沈征宇**
   ​    

   ```
   /*
       receive meeting application from frontend
    */
   @CrossOrigin(origins = "*")
   @PostMapping("/ApplyConference")
   public ResponseEntity<HashMap<String,Object>> applyMeeting(ApplyMeetingRequest request){
       //TODO:get token
       String token="";
       Long id=userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();
       logger.debug("ApplyMeetingForm: " + request.toString());
       HashMap<String,Object> map = new HashMap();
       ApplyMeeting applyMeeting=authService.applyMeeting(request,id);
       if (null == applyMeeting){
           map.put("message","会议申请失败，已有该会议");
           return ResponseEntity.ok(map);
       }else {
           map.put("token",token);
           map.put("message","success");
           map.put("user id",applyMeeting.getApplicantId());
           map.put("abbreviation",applyMeeting.getAbbreviation());
           map.put("fullName",applyMeeting.getFullName());
           map.put("holdingTime",applyMeeting.getHoldingTime());
           map.put("holdingPlace",applyMeeting.getHoldingPlace());
           map.put("submissionDeadline",applyMeeting.getSubmissionDeadline());
           map.put("reviewReleaseDate",applyMeeting.getReviewReleaseDate());
           return ResponseEntity.ok(map);
       }
   }
   ```

3.  数据层由于没有测试添加数据，运行方法里也没有添加数据表并添加默认数据的操作，请测试的同学对数据库进行配置后再进行测试

### 2020.3.21更新 沈征宇
1. 登录和注册页面不再需要权限

2. Token完善
```
       if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer "))
             jwtToken = requestTokenHeader.substring(7);
```

前端注意请求头必须是 Authorization:Token ${token}的形式

3. 实现登录和注册功能

4. 加密存储密码到数据库

先将转码器注册到主方法里
```
   @Bean
     public PasswordEncoder encoder() {
         return new BCryptPasswordEncoder();
     }
```
存储的时候转码
```
String password = passwordEncoder.encode(request.getPassword());
```
登录的时候用match匹配
```
            if (user.getUsername().equals(username) && passwordEncoder.matches(password,user.getPassword())){
                return "success";
            }
```
### 2020.3.20更新 沈征宇
1. 在SecurityConfig中配置基于openId的认证方式

2. 增强了Service的健壮性

### 2020.3.20更新 郭泰安

1. 修改了User实体类，删除了fullName,添加了 Email，institution, country,使其符合需求文档要求.  

2. 修改了数据库配置文件

   ​    

   ```
   #logging.level.root = DEBUG;
   logging.file.path=/var/tmp/mylog.log
   
   # 5 * 60 * 60 * 1000 = 5 hours
   jwt.token.validity=18000000
   jwt.token.secret=FdseFdse2020
   
   #集成mysql数据库的配置
   spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
   #project是数据库的名称
   spring.datasource.url=jdbc:mysql://localhost:3306/project?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
   #
   spring.datasource.username=root
   #密码与服务器密码一致
   spring.datasource.password=gysw2020
   #
   spring.jpa.hibernate.ddl-auto=update
   ### 建表方式
   spring.jpa.properties.hibernate.hbm2ddl.auto=update
   #
   spring.jpa.show-sql=true
   #
   spring.jackson.serialization.indent_output=true
   #
   server.port=8080
   ```

  

3. 已经测试的条目
   1. 后端项目能够正常运行，无报错异常。
   2. 项目运行后能够向数据库插入初始管理员的用户数据。
   
### 2020.3.19更新 沈征宇
1. 完善了JwtRequestFilter：过滤JWT请求，验证"Bearer token"格式，校验Token是否正确 

```
String token = jwtTokenUtil.generateToken(user);
map.put("token",token);
return ResponseEntity.ok(map);
```

2. 通过addCorsMappings解决跨域问题
```
registry.addMapping("/**")
        .allowedOrigins("*")
        .allowCredentials(true)
        .allowedMethods("GET", "POST", "DELETE", "PUT")
        .maxAge(3600);
```
3. 完善服务中响应注册的业务逻辑

### 2020.3.18更新 沈征宇
1. 完善了Service层对数据的操作
