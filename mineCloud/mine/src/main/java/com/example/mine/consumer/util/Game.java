package com.example.mine.consumer.util;

import com.alibaba.fastjson.JSONObject;
import com.example.mine.consumer.WebSocketServer;
import com.example.mine.pojo.Bot;
import com.example.mine.pojo.Record;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    private Integer inWalls;
    private Integer rows;
    private Integer cols;
    private int [][] g;
    final private static int[] vx = {-1, 0, 1, 0},vy={0, 1, 0, -1};
    private final Player A,B;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing";
    private String loser ="";
    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";
    public void setNextStepA(Integer nextStepA){
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        }finally {
            lock.unlock();
        }

    }
    private String getInput(Player player){
        Player me,you;
        if(player.getId().equals(A.getId())){
            me=A;
            you=B;
        }else {
            me=B;
            you=A;
        }
        return getMapString()
                +"#"+me.getSx()
                +"#"+me.getSx()
                +"#("+me.stepsToString()
                +")#"+you.getSx()
                +"#"+you.getSy()
                +"#("+you.stepsToString()+")";
    }
    private void sendBotCode(Player player){
       if(player.getBotId()==-1)return;
        MultiValueMap<String,String>data = new LinkedMultiValueMap<>();
        data.add("user_id",player.getId().toString());
        data.add("bot_code",player.getBotCode());
        data.add("input",getInput(player));
        WebSocketServer.restTemplate.postForObject(addBotUrl,data,String.class);
    }
    public boolean nextStep()  {
        try {
            Thread.sleep(500);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        sendBotCode(A);
        sendBotCode(B);
        for(int i=0;i<25;i++){
            try {
                Thread.sleep(200);
                lock.lock();
                try{
                    if(nextStepA!=null&&nextStepB!=null){
                        A.getSteps().add(nextStepA);
                        B.getSteps().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            }catch (InterruptedException e){
                throw  new RuntimeException(e);
            }
        }
        return false;
    }
    private String getMapString(){
        StringBuilder res=new StringBuilder();
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }
    private void saveToDataBase(){
        Record record = new Record(
                null,
                A.getId(),
                A.getSx(),
                A.getSy(),
                B.getId(),
                B.getSx(),
                B.getSy(),
                A.stepsToString(),
                B.stepsToString(),
                getMapString(),
                loser,
                new Date()

        );
        WebSocketServer.recordMapper.insert(record);
    }
    public void setNextStepB(Integer nextStepB){
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        }finally {
            lock.unlock();
        }
    }
    public Game(Integer inWalls, Integer rows, Integer cols, Integer idA, Integer idB, Bot aBot,Bot bBot){
        this.inWalls=inWalls;
        this.rows=rows;
        this.cols=cols;
        Integer aBotId=-1;
        String aBotCode="";
        Integer bBotId=-1;
        String bBotCode="";
        if(aBot!=null){
            aBotId=aBot.getId();
            aBotCode=aBot.getContent();
        }
        if(bBot!=null){
            bBotId=bBot.getId();
            bBotCode=bBot.getContent();
        }
        A = new Player(idA, rows - 2,1,aBotId,aBotCode,new ArrayList<>());
        B = new Player(idB,1,cols-2,bBotId,bBotCode,new ArrayList<>());
        g = new int [rows][cols];
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                g[i][j]=0;
            }
        }
    }
    public Player getPlayerA(){

        return A;
    }
    public Player getPlayerB(){

        return B;
    }
    private boolean check(int [][]pg, int sx,int sy,int ex,int ey){
       if(sx==ex&&sy==ey)return true;
       pg[sx][sy]=1;
       for(int i=0;i<4;i++) {
           int tx=sx+vx[i];
           int ty=sy+vy[i];
           if(pg[tx][ty]==0&&check(pg,tx,ty,ex,ey))return true;
       }
       return false;
    }
    public int[][] getMap(){
        return g;
    }
    public void createWalls(){
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.cols;j++){
                g[i][j]=0;
            }
        }
        for(int i=0;i<this.rows;i++){
            g[i][0]=1;
            g[i][this.cols-1]=1;
        }
        for(int i=0;i<this.cols;i++){
            g[0][i]=1;
            g[this.rows-1][i]=1;
        }
        Random rd =new Random();
        for(int i=0;i<this.inWalls/2;i++){
            while(true) {
                int r = rd.nextInt(this.rows);
                int c = rd.nextInt(this.cols);
                if(g[r][c]==1||r>c)continue;
                if(c==1&&r==this.rows-2||r==1&&c==this.cols-2)continue;
                g[r][c]=g[this.rows-1-r][this.cols-1-c]=1;
                break;
            }
        }
        int [][] pg =new int [this.rows][this.cols];
        for(int i=0;i<this.rows;i++) {
            for(int j=0;j<this.cols;j++){
                pg[i][j]=g[i][j];
            }
        }
        if(!check(pg,this.rows-2,1,1,this.cols-2))createWalls();
//        for(int i=0;i<this.rows;i++){
//            for(int j=0;j<this.cols;j++){
//                System.out.printf("%d ",g[i][j]);
//            }
//            System.out.println();
//        }
    }
    public void sendResult(){
        JSONObject resp = new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        saveToDataBase();
        sendAllMessage(resp.toJSONString());
    }
    public boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){
          int n= cellsA.size();
          Cell head = cellsA.get(n-1);
          if(g[head.x][head.y]==1)return false;
          for(int i=0;i<n-1;i++){
              Cell now = cellsA.get(i);
              if(now.x==head.x&&now.y== head.y)return false;
          }
          for(int i=0;i<n-1;i++){
              Cell now = cellsB.get(i);
              if(now.x==head.x&&now.y== head.y)return false;
          }
          return true;
    }
    public void judge(){
        List<Cell>cellsA = A.getCells();
        List<Cell>cellsB = B.getCells();
       boolean validA = check_valid(cellsA,cellsB);
       boolean validB = check_valid(cellsB,cellsA);
       if(!validA||!validB){
           status = "finished";
           if(!validA&&!validB)loser = "all";
           else if (!validA) {
               loser="A";
           }else {
               loser="B";
           }
       }

    }
    public void sendMove(){
        lock.lock();
        try{
            JSONObject resp = new JSONObject();
            resp.put("event","move");
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepB=nextStepA = null;
        }finally {
            lock.unlock();
        }
    }
    public void sendAllMessage(String message){
        if(WebSocketServer.users.get(A.getId())!=null)
        WebSocketServer.users.get(A.getId()).sendMessage(message);
        if(WebSocketServer.users.get(B.getId())!=null)
        WebSocketServer.users.get(B.getId()).sendMessage(message);
    }
    @Override
    public void run() {
       for(int i=0;i<1000;i++){
           if(nextStep()){
               judge();
               if(status.equals("playing")){
                   sendMove();
               }else{
                   sendResult();
                   break;
               }
           }else{
               this.status = "finished";
               lock.lock();
               try {
                   if (nextStepA == null && nextStepB == null) {
                       loser = "all";
                   } else if (nextStepB == null) {
                       loser = "B";
                   } else {
                       loser = "A";
                   }
                   sendResult();
               }finally {
                   lock.unlock();
               }
               break;
           }
       }
    }
}
