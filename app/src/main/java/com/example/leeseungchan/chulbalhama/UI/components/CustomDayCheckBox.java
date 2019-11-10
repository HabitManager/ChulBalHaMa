package com.example.leeseungchan.chulbalhama.UI.components;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.CheckBox;

import com.example.leeseungchan.chulbalhama.R;

import java.util.ArrayList;

public class CustomDayCheckBox{
    private ArrayList<CheckBox> boxes = new ArrayList<>();
    
    public CustomDayCheckBox(View view){
        boxes.add((CheckBox) view.findViewById(R.id.mon));
        boxes.add((CheckBox) view.findViewById(R.id.the));
        boxes.add((CheckBox) view.findViewById(R.id.wes));
        boxes.add((CheckBox) view.findViewById(R.id.thr));
        boxes.add((CheckBox) view.findViewById(R.id.fri));
        boxes.add((CheckBox) view.findViewById(R.id.sat));
        boxes.add((CheckBox) view.findViewById(R.id.sun));
    }
    
    public void getResult(final ArrayList<Boolean> result){
        for(int i = 0; i< 7; i++){
            if(result.size() <= i){
                if(boxes.get(i).isChecked()){
                    result.add(true);
                }else{
                    result.add(false);
                }
            }else{
                if(boxes.get(i).isChecked()){
                    result.set(i, true);
                }else{
                    result.add(i, false);
                }
            }
        }
    }

}
