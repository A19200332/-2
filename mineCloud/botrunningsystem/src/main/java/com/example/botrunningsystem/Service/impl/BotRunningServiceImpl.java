package com.example.botrunningsystem.Service.impl;

import com.example.botrunningsystem.Service.BotRunningService;
import com.example.botrunningsystem.Service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    public static BotPool botPool= new BotPool();
    @Override
    public String addBot(Integer userId, String botCode, String input) {
        System.out.println("addBot:"+userId+" "+botCode+" "+input);
        botPool.addBot(userId,botCode,input);
        return "add bot success";
    }
}
