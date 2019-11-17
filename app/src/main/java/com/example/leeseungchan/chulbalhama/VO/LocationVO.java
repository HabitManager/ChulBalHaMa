package com.example.leeseungchan.chulbalhama.VO;

import java.io.Serializable;

public class LocationVO implements Serializable {
    private int timeHour;
    private int timeMin;
    private double latitude;
    private double longitude;
    private String name;
    private String description;
    
    public LocationVO(){
        name = null;
        latitude = -1;
        longitude = -1;
        description = null;
        timeHour = -1;
        timeMin = -1;
    }
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    public void setTime(int timeHour, int timeMin) {
        this.timeHour = timeHour;
        this.timeMin = timeMin;
    }
    
    public String getTime(){
        return timeHour + " : " + timeMin;
    }
    
    public int getTimeHour() {
        return timeHour;
    }
    
    public int getTimeMin() {
        return timeMin;
    }
    
    public String getCoordinate(){
        return longitude + ", " + latitude;
    }
}
