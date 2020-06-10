package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.SubmitRebuttalRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-06-10 08:50
 **/
@CrossOrigin
@RestController
public class RebuttalController {

    @PostMapping("/submitRebuttal")
    public ResponseEntity<HashMap<String, Object>> submitRebuttal(HttpServletRequest httpServletRequest, @RequestBody SubmitRebuttalRequest submitRebuttalRequest, HttpServletResponse response) {
        HashMap<String, Object> map = new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        map.put("token",token);
                map.put("message","成功");
                map.put("reply",null);
                return ResponseEntity.ok(map);

    }
}