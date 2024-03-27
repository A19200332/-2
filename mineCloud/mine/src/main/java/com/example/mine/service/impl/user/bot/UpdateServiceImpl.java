package com.example.mine.service.impl.user.bot;

import com.example.mine.mapper.BotMapper;
import com.example.mine.pojo.Bot;
import com.example.mine.pojo.User;
import com.example.mine.service.impl.utils.UserDetailsImpl;
import com.example.mine.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> update(Map<String, String> data) {
        System.out.println(22333);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Integer botId = Integer.parseInt(data.get("bot_id"));
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");
        Bot bot = botMapper.selectById(botId);
        Map<String,String>map = new HashMap<>();
        System.out.println(title);
        if(bot == null){
            map.put("error_massage","bot不存在或已被删除");
            return map;
        }
        if(!bot.getUserId().equals(user.getId())){
            map.put("error_massage","无权限修改");
            return map;
        }
        if(title==null || title.length()==0){
            map.put("error_massage","标题不能为空");
            return map;
        }
        if(title.length()>100){
            map.put("error_massage","标题长度不能大于100");
            return map;
        }
        if(description == null||description.length()==0){
            description = "该用户很懒,什么也没留下";
        }
        if(description.length()>300){
            map.put("error_massage","描述长度不能大于300");
            return map;
        }
        if(content == null||content.length()==0){
            map.put("error_massage","代码不能为空");
            return map;
        }
        if(content.length()>10000){
            map.put("error_massage","代码长度不能大于10000");
            return map;
        }
        Bot newBot =new Bot(
             bot.getId(),
             bot.getUserId(),
             title,
             description,
             content,
             bot.getCreatetime(),
             new Date()
        );
        botMapper.updateById(newBot);
        map.put("error_massage","success");
        return map;
    }
}
