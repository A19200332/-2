package com.example.mine.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
//@Controller
@RestController
@RequestMapping("/pk/")
public class BotInfoController {
    @RequestMapping("get/")
    public Map<String,String> get(){
        Map<String,String>m=new HashMap<>();
        m.put("name","tiger");
        m.put("rating","1500");
        return m;
    }
}
