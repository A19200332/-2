package com.example.matchingsystem.service.impl;


import com.example.matchingsystem.service.MatchingService;
import com.example.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool=new MatchingPool();
    @Override
    public String addPlayer(Integer userId, Integer rating,Integer botId) {
        System.out.println("userId:"+userId+" "+"rating:"+rating+"botId:"+botId);
        matchingPool.addPlayer(userId,rating,botId);
        return "addSuccess";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("userId:"+userId);
        matchingPool.removePlayer(userId);
        return "removeSuccess";
    }
}
