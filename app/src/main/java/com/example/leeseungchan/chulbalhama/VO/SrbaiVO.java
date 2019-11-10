package com.example.leeseungchan.chulbalhama.VO;

public class SrbaiVO {
    int id;
    String day;
    int score;
    int habitId;
    
    public SrbaiVO(String day, int score, int habitId){
        this.day = day;
        this.score = score;
        this.habitId = habitId;
    }
    
    public int getScore() {
        return score;
    }
    
    public String getDay() {
        return day;
    }
    
    public int getHabitId() {
        return habitId;
    }
}
