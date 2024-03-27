package com.example.botrunningsystem.Service.impl.utils;

import com.example.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
@Component
public class Consumer extends Thread{
    private Bot bot;
    public static RestTemplate restTemplate;
    String botMoveUrl ="http://127.0.0.1:3000/bot/receive/move/";
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        Consumer.restTemplate=restTemplate;
    }
    public void StartTimeOut(long timeOut,Bot bot) {
        this.bot = bot;
        this.start();
        try {
            this.join(timeOut);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            this.interrupt();
        }
    }
    private String addUid(String code,String uid){
        int k=code.indexOf(" implements com.example.botrunningsystem.utils.BotInterface{");
        String ans = code.substring(0,k)+uid+code.substring(k);
        return ans;
    }
    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid =uuid.toString().substring(0,8);
        BotInterface botInterface = Reflect.compile(
                "com.example.botrunningsystem.utils.Bot"+uid,
                addUid(bot.getBotCode(),uid)

        ).create().get();
        Integer direction =botInterface.nextMove(bot.getInput());
        MultiValueMap<String,String>data= new LinkedMultiValueMap<>();
        data.add("user_id",bot.getUserId().toString());
        data.add("direction",direction.toString());
        restTemplate.postForObject(botMoveUrl,data,String.class);
        System.out.println("move"+bot.getUserId()+" "+direction);
    }
}
