package com.example.leeseungchan.chulbalhama.UI.habit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class HabitChangeFragment extends Fragment {
    
    public Bundle bundle = new Bundle();
    
    private HabitsVO habit;
    private DBHelper dbHelper;
    private ArrayList<String> prepare = new ArrayList<>();
    private ArrayList<Boolean> days = new ArrayList<>();
    
    public static HabitChangeFragment newInstance(Bundle bundle){
        HabitChangeFragment v = new HabitChangeFragment();
        v.bundle = bundle;
        return v;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_habit, container, false);
    
        dbHelper = new DBHelper(getContext());
        habit = (HabitsVO) bundle.getSerializable("habit");
        
        
        /* habit name */
        LinearLayout habitName = v.findViewById(R.id.name_setting);
        TextView nameText = habitName.findViewById(R.id.item_name);
        nameText.setText(habit.getHabitName());
        Button nameChange = habitName.findViewById(R.id.button_change);
        nameChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setNameDialog( v, "이름 바꾸기", habit.getId(), "habit_name");
            }
        });
        Button nameDelete = habitName.findViewById(R.id.button_delete);
        nameDelete.setVisibility(View.GONE);
    
        /* habit desc */
        LinearLayout habitDesc = v.findViewById(R.id.desc_setting);
        TextView descText = habitDesc.findViewById(R.id.item_name);
        descText.setText(habit.getDescription());
        Button descChange = habitDesc.findViewById(R.id.button_change);
        descChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setNameDialog( v, "설명 바꾸기", habit.getId(), "description");
            }
        });
        Button descDelete = habitDesc.findViewById(R.id.button_delete);
        descDelete.setVisibility(View.GONE);
    
        /* prepare list*/
        RecyclerView prepares = v.findViewById(R.id.list);
        RecyclerView.LayoutManager layoutManager =
            new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        prepares.setLayoutManager(layoutManager);
    
        RecyclerView.Adapter prepareAdapter = new PrepareAdapter(prepare);
        prepares.setAdapter(prepareAdapter);
    
        setPrepare(habit.getPrepare());
    
        /* habit days */
        LinearLayout habitday = v.findViewById(R.id.day_setting);
    
    
        final CustomSevenDayInfo customSevenDayInfo =
            new CustomSevenDayInfo(v.findViewById(R.id.add_habit_day));
        customSevenDayInfo.setPlaceData();
    
        TextView dayText = habitday.findViewById(R.id.item_name);
        dayText.setText(habit.getHabitName());
        Button dayChange = habitday.findViewById(R.id.button_change);
        dayChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DayDialog customDialog = new DayDialog(getActivity());
            
                customDialog.callFunction(days, customSevenDayInfo);
            }
        });
        Button dayDelete = habitday.findViewById(R.id.button_delete);
        dayDelete.setVisibility(View.GONE);
        return v;
    }
    
    public void setNameDialog(View v, String title, final int id, final String attr){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        View viewInflated =
            LayoutInflater.from(getContext()).inflate(
                R.layout.dialog_edit_text,
                (ViewGroup) getView(),
                false
            );
        
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String newName = input.getText().toString();
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String sql = "update habits set"+ " "+ attr +"=\""+ newName + "\" where _id=" + id;
                db.execSQL(sql);
                db.close();
                Log.e("name input", "onClick:" + newName);
            }
        });
        
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        
        builder.show();
    }
    
    private void setPrepare(String prepare){
        String[] prepares = prepare.split(",");
        
        this.prepare.clear();
        
        for(int i=0; i<prepares.length; i++){
            this.prepare.add(prepares[i]);
        }
        
    }
}
