package com.example.leeseungchan.chulbalhama.VO;

import java.io.Serializable;

public class HabitsVO implements Serializable {
    int id;
    String habitName;
    int due;
    String prepare;
    int achievementRate;

    public HabitsVO(){}

    public HabitsVO(int id, String habitName, int due, String prepare, int achievementRate){
        this.id = id;
        this.habitName = habitName;
        this.due = due;
        this.prepare = prepare;
        this.achievementRate = achievementRate;
    }

    public int getId(){
        return id;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }
    
    public int getDue() {
        return due;
    }
    
    public void setDue(int due) {
        this.due = due;
    }
    
    public String getPrepare() {
        return prepare;
    }

    public void setPrepare(String prepare) {
        this.prepare = prepare;
    }
}
