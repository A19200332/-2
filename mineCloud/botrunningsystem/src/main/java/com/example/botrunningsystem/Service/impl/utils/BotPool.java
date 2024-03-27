package com.example.botrunningsystem.Service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread{
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Queue<Bot> bots = new LinkedList<>();
    public void addBot(Integer userId,String botCode,String input){
        Bot bot = new Bot(userId,botCode,input);
        lock.lock();
        try{
            bots.add(bot);
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.StartTimeOut(2000,bot);
    }
    @Override
    public void run() {
        while(true){
            lock.lock();
            if(bots.isEmpty()){
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }else{
                Bot bot = bots.remove();
                lock.unlock();
                consume(bot);
            }
        }
    }


}
