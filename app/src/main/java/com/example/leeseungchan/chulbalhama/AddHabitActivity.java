package com.example.leeseungchan.chulbalhama;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddHabitActivity extends AppCompatActivity{

    private RadioButton needButton, noNeedButton;
    private RadioGroup prepareRadioGroup;


    private CustomRadioButton[] btn1 = new CustomRadioButton[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);


        // set up toolbar on top
        Toolbar toolbarMain = findViewById(R.id.toolbar_add_habit);
        setSupportActionBar(toolbarMain);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // set up radio group
        needButton = findViewById(R.id.prepare_need);

        noNeedButton = findViewById(R.id.prepare_no_need);
        noNeedButton.setChecked(true);

        prepareRadioGroup = findViewById(R.id.prepare_group);

        LinearLayout week1 = findViewById(R.id.week1);
        makeWeekButton(week1);


    }

    // radio group click listener
    RadioGroup.OnCheckedChangeListener prepareGroupChangeListner =
            new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == R.id.prepare_need){
                // @todo implement resolving input form
            }else if(checkedId == R.id.prepare_no_need){
                // @todo implement hiding input form
            }
        }
    };

    private void makeWeekButton(LinearLayout layout){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        String[] week = {"일", "월","화", "수","목","금","토"};
        for(int i = 0; i < btn1.length; i++){
            btn1[i] = new CustomRadioButton(this, null);
            btn1[i].setText(week[i]);
            if(i == 0)
                btn1[i].setTextColor(Color.RED);
            if(i == btn1.length - 1)
                btn1[i].setTextColor(Color.BLUE);
            btn1[i].setLayoutParams(params);
            layout.addView(btn1[i]);
        }
    }
}