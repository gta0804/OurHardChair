package fudan.se.lab2.controller;

import org.hibernate.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-04-30 17:03
 **/
@CrossOrigin()
@Controller
public class PdfController {
    Logger logger = LoggerFactory.getLogger(PdfController.class);

    @RequestMapping(value = "/preview/{fileName}", method = RequestMethod.GET)
    public void pdfStreamHandler(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) {
        //PDF文件地址
        logger.info("文件名:" + fileName);
                //"src/main/resources/pdf/2002260.pdf"
//        File file = new File("/workplace/upload/" +fileName);
        File file = new File("/workplace/upload/" +fileName);

        logger.info(file.toString());
        if (file.exists()) {
            byte[] data = null;
            FileInputStream input=null;
            try {
                input= new FileInputStream(file);
                data = new byte[input.available()];
                int btes = input.read(data);
                response.getOutputStream().write(data);
            } catch (Exception e) {
                System.out.println("pdf文件处理异常：" + e);
            }finally{
                try {
                    if(input!=null){
                        input.close();
                    }
                } catch (IOException e) {
                    logger.debug("失败");
                }
            }
        }
    }
}
