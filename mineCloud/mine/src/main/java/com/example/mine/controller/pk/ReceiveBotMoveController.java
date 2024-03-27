package com.example.mine.controller.pk;

import com.example.mine.service.pk.ReceiveBotMove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ReceiveBotMoveController {
    @Autowired
    private ReceiveBotMove receiveBotMove;
    @PostMapping("/bot/receive/move/")
    public void botMove(@RequestParam MultiValueMap<String,String>data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer direction =Integer.parseInt(Objects.requireNonNull(data.getFirst("direction")));
        System.out.println("receive controller");
        receiveBotMove.botMove(userId,direction);
    }
}
