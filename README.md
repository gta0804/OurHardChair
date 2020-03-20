## 后端更新日志

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