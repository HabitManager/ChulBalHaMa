package com.example.leeseungchan.chulbalhama.VO;


import java.io.Serializable;
import java.util.ArrayList;

public class DatasetVO implements Serializable {
    public ArrayList<Integer> days;
    public ArrayList<Double> scores;
    
    public void setDaysNScores(ArrayList<Integer> days, ArrayList<Double> scores){
        this.days=days;
        this.scores=scores;
    }
    
    public ArrayList<Integer> getDays(){
        return days;
    }
    public ArrayList<Double> getScores(){
        return scores;
    }
    
}