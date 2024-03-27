package com.example.mine.service.impl.pk;

import com.example.mine.consumer.WebSocketServer;
import com.example.mine.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer bId,Integer aBotId,Integer bBotId) {
        System.out.println("startGame:"+aId+" "+bId+" "+aBotId+" "+bBotId);
        WebSocketServer.startGame(aId,bId,aBotId,bBotId);
        return "start game success";
    }
}
