package com.example.leeseungchan.chulbalhama.Activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.Adpater.PrepareAdapter;
import com.example.leeseungchan.chulbalhama.DBHelper;
import com.example.leeseungchan.chulbalhama.DayDialog;
import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.UI.components.CustomSevenDayInfo;
import com.example.leeseungchan.chulbalhama.VO.HabitsVO;

import java.util.ArrayList;

public class AddHabitActivity extends AppCompatActivity{

    private EditText habitName;
    private EditText habitDesc;
    // prepare
    private ArrayList<String> prepares = new ArrayList<>();
    // day and place
    private ArrayList<Boolean> days = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        TextView guideText;
        // set up toolbar on top
        setToolbar();

        habitName = findViewById(R.id.add_habit_name);
        habitDesc = findViewById(R.id.add_habit_desc);

        /* prepare */
        final LinearLayout prepare = findViewById(R.id.prepare);

        // prepare TextView guide text
        guideText = prepare.findViewById(R.id.guide_for_selection);
        guideText.setText(R.string.guide_ask_prepare);

        // prepare recycler view
        final RecyclerView prepareRecycle = prepare.findViewById(R.id.list);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        prepareRecycle.setLayoutManager(layoutManager);

        final RecyclerView.Adapter prepareAdapter = new PrepareAdapter(prepares);
        prepareRecycle.setAdapter(prepareAdapter);

        Button prepareInputButton = prepare.findViewById(R.id.button_for_selection);
        prepareInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = prepare.findViewById(R.id.input_for_selection);
                if(text.getText().toString().length() < 1){
                    text.setHint("1자 이상 입력해주세요");
                    text.setHintTextColor(Color.RED);
                }else {
                    prepares.add(text.getText().toString());
                    text.setText("");
                    text.setHint("여기에 입력해 주세요.");
                    text.setHintTextColor(Color.GRAY);
                    prepareAdapter.notifyDataSetChanged();
                }
            }
        });


        /* day and place input*/
        final LinearLayout dayPlace = findViewById(R.id.add_habit_intro);

        final CustomSevenDayInfo customSevenDayInfo =
                new CustomSevenDayInfo(findViewById(R.id.add_habit_day));
        customSevenDayInfo.setPlaceData();

        // day and place TextView guide text
        guideText = dayPlace.findViewById(R.id.item_name);
        guideText.setText(R.string.guide_date_and_place_intro);

        TextView text = dayPlace.findViewById(R.id.item_description);
        text.setVisibility(View.INVISIBLE);

        // day and place Button to add
        Button dayPlaceInputButton = dayPlace.findViewById(R.id.button_change);
        dayPlaceInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayDialog customDialog = new DayDialog(AddHabitActivity.this);

                customDialog.callFunction(days, customSevenDayInfo);
            }
        });

        Button dayDelInputButton = dayPlace.findViewById(R.id.button_delete);
        dayPlace.removeView(dayDelInputButton);
        
        Button store = findViewById(R.id.store_habit);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertHabit();
                finish();
            }
        });
    }
    
    private void setToolbar(){
        Toolbar toolbarMain = findViewById(R.id.toolbar_add_habit);
        setSupportActionBar(toolbarMain);
    
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    

    private void serializeHabit(HabitsVO habit){
        String habitName = this.habitName.getText().toString();
        String habitDesc = this.habitDesc.getText().toString();
        StringBuffer prepare = new StringBuffer();
        for(int i = 0; i < prepares.size(); i++){
            prepare.append(prepares.get(i) + ",");
        }

        habit.setHabitName(habitName);
        habit.setDescription(habitDesc);
        habit.setPrepare(prepare.toString());
    }
    
    private void insertHabit(){
        HabitsVO habitsVO = new HabitsVO();
        serializeHabit(habitsVO);
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(
            "insert into habits (habit_name, description, prepare) values(?,?,?)",
            new Object[]{
                habitsVO.getHabitName(),
                habitsVO.getDescription(),
                habitsVO.getPrepare()}
            );
        db.close();
    }

}
