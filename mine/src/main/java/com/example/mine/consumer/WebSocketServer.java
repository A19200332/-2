package com.example.mine.consumer;
import com.alibaba.fastjson.JSONObject;
import com.example.mine.consumer.util.Game;
import com.example.mine.consumer.util.JwtAuthentication;
import com.example.mine.mapper.RecordMapper;
import com.example.mine.mapper.UserMapper;
import com.example.mine.pojo.User;
import com.example.mine.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    public static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();
    private User user;
    private Session session;
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;

    private Game game =null;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws Exception{
        this.session=session;
        // 建立连接
        System.out.println("connected!");
        Integer userId = JwtAuthentication.getUser(token);

        this.user = userMapper.selectById(userId);
        if(this.user!=null) {
            users.put(userId, this);
        }else{
            session.close();
        }
//        System.out.println(user);
    }

    @OnClose
    public void onClose() {
        System.out.println("disconnected");
        if(this.user!=null){
            users.remove(this.user.getId());
        }
        // 关闭链接
    }
    public void startMatching(){
        System.out.println("start-matching");
        matchPool.add(this.user);
        while(matchPool.size()>=2){
            Iterator<User> it = matchPool.iterator();
            User a = it.next(),b = it.next();
            Game game =new Game(20,13,14,a.getId(),b.getId());
            game.createWalls();


            users.get(a.getId()).game = game;
            users.get(b.getId()).game = game;
            game.start();
            JSONObject respGame = new JSONObject();
            respGame.put("a_id",game.getPlayerA().getId());
            respGame.put("a_sx",game.getPlayerA().getSx());
            respGame.put("a_sy",game.getPlayerA().getSy());

            respGame.put("b_id",game.getPlayerB().getId());
            respGame.put("b_sx",game.getPlayerB().getSx());
            respGame.put("b_sy",game.getPlayerB().getSy());

            respGame.put("game_map",game.getMap());
            JSONObject Aresp = new JSONObject();
            Aresp.put("event","start-matching");
            Aresp.put("opponent_username",b.getUsername());
            Aresp.put("opponent_photo",b.getPhoto());
            Aresp.put("game",respGame);
            users.get(a.getId()).sendMessage(Aresp.toJSONString());
            JSONObject Bresp = new JSONObject();
            Bresp.put("event","start-matching");
            Bresp.put("opponent_username",a.getUsername());
            Bresp.put("opponent_photo",a.getPhoto());
            Bresp.put("game",respGame);
            users.get(b.getId()).sendMessage(Bresp.toJSONString());
            matchPool.remove(a);
            matchPool.remove(b);

        }
    }

    public void move(Integer direction){
        System.out.println(2);
        if(game.getPlayerA().getId().equals(user.getId())){
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user.getId())){
            game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive_massage");
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        }else if("stop-matching".equals(event)){
            stopMatching();
        }else if("move".equals(event)){
            System.out.println(1);
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    public void sendMessage(String message){
        synchronized (this.session){
            try{
                this.session.getBasicRemote().sendText(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}