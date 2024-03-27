package com.example.mine.service.impl.user.bot;

import com.example.mine.mapper.BotMapper;
import com.example.mine.pojo.Bot;
import com.example.mine.pojo.User;
import com.example.mine.service.impl.utils.UserDetailsImpl;
import com.example.mine.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user = loginUser.getUser();
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");
        Map<String,String>mp = new HashMap<>();
        System.out.println(title);
        if(title==null || title.length()==0){
            mp.put("error_massage","标题不能为空");
            return mp;
        }
        if(title.length()>100){
            mp.put("error_massage","标题长度不能超过100");
            return mp;
        }
        if(description==null||description.length()==0){
            description = "这个用户很懒，什么也没有写";
        }
        System.out.println(description);
        if(description.length()>300){
            mp.put("error_massage","描述长度不能超过300");
            return mp;
        }
        if(content==null || content.length()==0){
            mp.put("error_massage","代码不能为空");
            return mp;
        }
        if(content.length()>10000){
            mp.put("error_massage","代码长度不能超过10000");
            return mp;
        }
        Date now = new Date();
        Bot bot = new Bot(null,user.getId(),title,description,content,now,now);
        botMapper.insert(bot);
        mp.put("error_massage","success");
        return mp;
    }
}
