package com.example.mine.service.impl.user.bot;

import com.example.mine.mapper.BotMapper;
import com.example.mine.pojo.Bot;
import com.example.mine.pojo.User;
import com.example.mine.service.impl.utils.UserDetailsImpl;
import com.example.mine.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> remove(Map<String, String> data) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user =  loginUser.getUser();
        Integer botId = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(botId);
        Map<String,String> map = new HashMap<>();
        if(bot == null){
            map.put("error_massage","bot不存在或已被删除");
            return map;
        }
        if(!bot.getUserId().equals(user.getId())){
            map.put("error_massage","无权删除该bot");
            return map;
        }
        botMapper.deleteById(botId);
        map.put("error_massage","success");
        return map;
    }
}
