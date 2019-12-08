package com.example.leeseungchan.chulbalhama.VO;

public class UserVO {
    int id;
    String startingCoordinate;
    String name;
    String startingName;
    
    public UserVO(String coordinate, String startingName, String name){
        startingCoordinate = coordinate;
        this.name = name;
        this.startingName = startingName;
    }
    
    public String getName() {
        return name;
    }
    
    public String getStartingCoordinate() {
        return startingCoordinate;
    }
    
    public String getStartingName(){
        return startingName;
    }
}
