package com.example.mine.consumer.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
    private List<Integer>steps;
    private boolean check_tail_increasing(int steps){
        if(steps<=10)return true;
        return steps%3==1;
    }
    public List<Cell> getCells(){
        List <Cell> res = new ArrayList<>();
        int []dy ={0, 1, 0, -1},dx={-1, 0, 1, 0};
        int x = sx,y =sy;
        res.add(new Cell(x,y));
        int step=0;
        for(int d :steps){
            x+=dx[d];
            y+=dy[d];
            res.add(new Cell(x,y));
            if(!check_tail_increasing(++step))res.remove(0);
        }
        return res;
    }
    public String stepsToString(){
        StringBuilder res =new StringBuilder();
        for(int d:steps){
            res.append(d);
        }
        return res.toString();
    }
}
