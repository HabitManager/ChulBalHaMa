package com.example.leeseungchan.chulbalhama.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;


import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.UI.location_info.DestinationInfoFragment;
import com.example.leeseungchan.chulbalhama.UI.location_info.StartingPointInfoFragment;

import java.util.ArrayList;

public class LocationInfoActivity extends AppCompatActivity{

    private int timeHour;
    private int timeMin;
    private double latitude;
    private double longitude;
    private String name;
    private String description;
    private ArrayList<String> dayOfWeekTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        Intent intent = getIntent();
        int data = intent.getIntExtra("type", 1);

        name = null;
        latitude = -1;
        longitude = -1;
        description = null;
        timeHour = -1;
        timeMin = -1;
        dayOfWeekTime = null;

        // set up toolbar on top
        Toolbar toolbarMain = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarMain);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //set up destination Info
        DestinationInfoFragment destinationInfoFragment = new DestinationInfoFragment();
        final StartingPointInfoFragment startingPointInfoFragment = new StartingPointInfoFragment();
    
        FragmentManager fragmentManager = getSupportFragmentManager();
    
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(data == 1) {
            transaction.replace(R.id.nav_host_fragment, destinationInfoFragment)
                    .commitAllowingStateLoss();

            setTitle(R.string.title_destination);
        } else {
            transaction.replace(R.id.nav_host_fragment, startingPointInfoFragment)
                    .commitAllowingStateLoss();

            setTitle(R.string.title_starting);
        }

    }

    @Override
    public void setTitle(int id){
        TextView title = (TextView)findViewById(R.id.toolbar_title);
        title.setText(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeHour() {
        return timeHour;
    }

    public void setTimeHour(int timeHour) {
        this.timeHour = timeHour;
        Log.d(null,Integer.toString(timeHour));
    }

    public int getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(int timeMin) {
        this.timeMin = timeMin;
    }

    public ArrayList<String> getDayOfWeekTime() {
        return dayOfWeekTime;
    }

    public void setDayOfWeekTime(ArrayList dayOfWeekTime) {
        this.dayOfWeekTime = dayOfWeekTime;
    }
}
