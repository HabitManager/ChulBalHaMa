package com.example.leeseungchan.chulbalhama.VO;

import com.example.leeseungchan.chulbalhama.Adpater.DestinationAdapter;

public class DestinationsVO {

    int id;
    String coordinate;
    String time;
    String destinationName;

    public DestinationsVO(){}
    
    public DestinationsVO(String coordinate, String time, String destinationName){
        this.coordinate = coordinate;
        this.time = time;
        this.destinationName = destinationName;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getCoordinate() {
        return coordinate;
    }
    
    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public String getDestinationName(){
        return destinationName;
    }

    public void setDestinationName(String name){
        destinationName = name;
    }

}