/*
* @author LeeSC
* @todo make constant to choose fragment
*
*/

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
import com.example.leeseungchan.chulbalhama.VO.DestinationsVO;
import com.example.leeseungchan.chulbalhama.VO.LocationVO;

import java.util.ArrayList;

public class LocationInfoActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 1);

        // set up toolbar on top
        Toolbar toolbarMain = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarMain);
        
        if(type == 1){
            setTitle(R.string.title_destination);
        } else if(type == 0){
            setTitle(R.string.title_starting);
        }
    
        // show fragment
        showLocationInfoFragment(type);
    }

    @Override
    public void setTitle(int id){
        TextView title = (TextView)findViewById(R.id.toolbar_title);
        title.setText(id);
    }
    
    private Bundle makeBundle(LocationVO locationVO, ArrayList<String> dayOfWeekTime){
        Bundle bundle = new Bundle();
        bundle.putSerializable("locationVO", locationVO);
        bundle.putStringArrayList("dayOfWeekTime", dayOfWeekTime);
        return bundle;
    }
    
    private void showLocationInfoFragment(int type){
        
        // data to show
        LocationVO locationVO = new LocationVO();
        ArrayList<String> dayOfWeekTime = null;
        Bundle bundle = makeBundle(locationVO, dayOfWeekTime);
        
        // candidates to show as fragment
        DestinationInfoFragment destinationInfoFragment;
        StartingPointInfoFragment startingPointInfoFragment;
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        // make Instance of fragment and show fragment.
        if(type == 1) {
            destinationInfoFragment = DestinationInfoFragment.newInstance(bundle);
            transaction.replace(R.id.nav_host_fragment, destinationInfoFragment)
                .commitAllowingStateLoss();
            
        } else {
            startingPointInfoFragment  = StartingPointInfoFragment.newInstance(bundle);
            transaction.replace(R.id.nav_host_fragment, startingPointInfoFragment)
                .commitAllowingStateLoss();
        }
    }
}
