package com.example.mine.service.impl.pk;

import com.example.mine.consumer.WebSocketServer;
import com.example.mine.consumer.util.Game;
import com.example.mine.consumer.util.Player;
import com.example.mine.service.pk.ReceiveBotMove;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveImpl implements ReceiveBotMove {
    @Override
    public void botMove(Integer userId, Integer direction) {
        System.out.println("receive service"+userId+" "+direction);
       if(WebSocketServer.users.get(userId)!=null){
           System.out.println("socket");
           Game game=WebSocketServer.users.get(userId).game;
           if(game!=null) {
               System.out.println("game");
               Player A = game.getPlayerA();
               Player B = game.getPlayerB();
               if (userId.equals(A.getId())) {
                   System.out.println("A");
                  game.setNextStepA(direction);
               } else if (userId.equals(B.getId())) {
                   System.out.println("B");
                   game.setNextStepB(direction);
               }
           }
       }
    }
}
