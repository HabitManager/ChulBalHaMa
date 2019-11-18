package com.example.leeseungchan.chulbalhama.Activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.UI.habit.HabitChangeFragment;
import com.example.leeseungchan.chulbalhama.UI.habit.HabitHistoryFragment;
import com.example.leeseungchan.chulbalhama.VO.HabitsVO;

public class HabitInfoActivity extends AppCompatActivity {

    private HabitsVO habit;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_habit);
        habit = (HabitsVO) getIntent().getSerializableExtra("habit");
        type = getIntent().getIntExtra("type", 0);
        
        Bundle bundle = new Bundle();
        bundle.putSerializable("habit", habit);
        

        // set up toolbar on top
        Toolbar toolbarMain = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarMain);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setTitle(habit.getHabitName());
    
        HabitChangeFragment habitChangeFragment = HabitChangeFragment.newInstance(bundle);
        HabitHistoryFragment habitHistoryFragment = HabitHistoryFragment.newInstance(bundle);
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(type == 0) {
            transaction.replace(R.id.nav_host_fragment, habitChangeFragment)
                .commitAllowingStateLoss();
        
        } else {
            transaction.replace(R.id.nav_host_fragment, habitHistoryFragment)
                .commitAllowingStateLoss();
        
        }
    }
    
    
    public void setTitle(String id){
        TextView title = (TextView)findViewById(R.id.toolbar_title);
        title.setText(id);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
