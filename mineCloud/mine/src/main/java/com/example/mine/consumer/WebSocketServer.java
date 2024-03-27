package com.example.mine.consumer;

import com.alibaba.fastjson.JSONObject;
import com.example.mine.consumer.util.Game;
import com.example.mine.consumer.util.JwtAuthentication;
import com.example.mine.mapper.BotMapper;
import com.example.mine.mapper.RecordMapper;
import com.example.mine.mapper.UserMapper;
import com.example.mine.pojo.Bot;
import com.example.mine.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    public static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();
    private User user;
    private Session session;
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    public static BotMapper botMapper;
    public static RestTemplate restTemplate;
    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";
    public Game game =null;
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    }
    @Autowired
    public void setBotMapper(BotMapper botMapper){
        WebSocketServer.botMapper = botMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate = restTemplate;
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
    public static void startGame(Integer aId,Integer bId,Integer aBotId,Integer bBotId){
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);
        Bot aBot = botMapper.selectById(aBotId);
        Bot bBot = botMapper.selectById(bBotId);
        Game game =new Game(20,13,14,a.getId(),b.getId(),aBot,bBot);
        game.createWalls();

        if(users.get(a.getId())!=null)
        users.get(a.getId()).game = game;
        if(users.get(b.getId())!=null)
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
        if(users.get(a.getId())!=null)
        users.get(a.getId()).sendMessage(Aresp.toJSONString());
        JSONObject Bresp = new JSONObject();
        Bresp.put("event","start-matching");
        Bresp.put("opponent_username",a.getUsername());
        Bresp.put("opponent_photo",a.getPhoto());
        Bresp.put("game",respGame);
        if(users.get(b.getId())!=null)
        users.get(b.getId()).sendMessage(Bresp.toJSONString());
    }
    public void startMatching(String botId){
        System.out.println("start-matching");
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        data.add("bot_id",botId);
        restTemplate.postForObject(addPlayerUrl,data,String.class);

    }
    public void stopMatching(){
        System.out.println("stop-matching");
        MultiValueMap<String,String>data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);
    }

    public void move(Integer direction){
        if(game.getPlayerA().getId().equals(user.getId())){
            if(game.getPlayerA().getBotId()==-1)
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user.getId())){
            if(game.getPlayerB().getBotId()==-1)
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
            startMatching(data.getString("bot_id"));
        }else if("stop-matching".equals(event)){
            stopMatching();
        }else if("move".equals(event)){
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