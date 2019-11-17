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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;


import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.UI.components.CustomChangeDeleteItem;
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
        setToolbar(R.id.toolbar_main);
        
        if(type == 1){
            setTitle(R.string.title_destination);
        } else if(type == 0){
            setTitle(R.string.title_starting);
        }
    
        // show fragment
        showLocationInfoFragment(type);
    }
    private void setToolbar(int toolbarId){
        Toolbar toolbarMain = findViewById(toolbarId);
        setSupportActionBar(toolbarMain);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
    
    public void setAddress(CustomChangeDeleteItem item, String address){
        if(address == null)
            item.setTitle(getResources().getString(R.string.guide_address));
        else
            item.setTitle(address);
    }
    
    public void setTime(CustomChangeDeleteItem time, LocationVO locationVO){
        int time_hour = locationVO.getTimeHour();
        int time_minute = locationVO.getTimeMin();
        
        if(time_hour > 0 && time_minute > 0){
            time.setTitle(time_hour + ":" + time_minute);
        }else{
            time.setTitle(getResources().getString(R.string.guide_when_time));
        }
    }
    
    public void setEditTextText(EditText editText, LocationVO locationVO){
        String name = locationVO.getName();
        if(name != null) {
            editText.setText(name);
        }
        setNameListener(editText, locationVO);
    }
    
    private void setNameListener(EditText edit, final LocationVO locationVO){
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(Editable arg0) {
                locationVO.setName(arg0.toString());
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
    }
}
