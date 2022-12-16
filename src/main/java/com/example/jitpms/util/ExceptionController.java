package com.example.jitpms.util;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map globalException(HttpServletRequest request, Exception e) {
        Map<String,Object> map = new HashMap<>();
        map.put("code",500);
        map.put("msg",e.getMessage());
        System.out.println("异常");
        e.printStackTrace();
        return map;
    }

}
