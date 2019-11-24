package com.example.leeseungchan.chulbalhama.Service;

import android.os.Handler;
import android.util.Log;

public class LocationUpdateThread extends Thread {
    Handler updateHandler;
    boolean isRun = true;
    private  int timeInterval=10000;

    public LocationUpdateThread(Handler updateHandler){ this.updateHandler = updateHandler;}

    public void stopForever(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        while(isRun){
            updateHandler.sendEmptyMessage(0);
            try{
                Log.d("쓰레드!", "돈다!");
                Thread.sleep(timeInterval);
            }catch (Exception e) { Log.e("쓰레드!", "큰일이야!");}
        }
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int interval){
        this.timeInterval = interval;
    }
}
