package com.example.leeseungchan.chulbalhama;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.leeseungchan.chulbalhama.UI.components.CustomDayCheckBox;
import com.example.leeseungchan.chulbalhama.UI.components.CustomSevenDayInfo;

import java.util.ArrayList;


public class DayDialog {
    private Context context;

    public DayDialog(Context context) {
        this.context = context;
    }

    public void callFunction(final ArrayList<Boolean> result,
                             final CustomSevenDayInfo customSevenDayInfo,
                             final int habitId,
                             int mode) {

        final Dialog dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dlg.setContentView(R.layout.dialog_day);

        dlg.show();

        final CustomDayCheckBox dayCheckBox =
                new CustomDayCheckBox(dlg.findViewById(R.id.custom_days_checkbox));
        if(mode == 0) {
            dayCheckBox.setBoxes(result);
        }else{
            dayCheckBox.setBoxes(result, mode);
        }
        dayCheckBox.showSelectedBoxes(habitId);
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Boolean> list = new ArrayList<>();
                dayCheckBox.getResult(list);
                customSevenDayInfo.pickDay(list);
                customSevenDayInfo.updateDayHabit(result, habitId);
                customSevenDayInfo.deleteDayHabit(compare(result, list), habitId);
                duplicateList(result, list);
                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
            }
        });
    }
    
    private ArrayList<Boolean> compare(ArrayList<Boolean> old, ArrayList<Boolean> newOne){
        ArrayList<Boolean> temp = new ArrayList<>();
        for(int i = 0; i < old.size(); i++){
            temp.add(false);
            if(old.get(i) && !newOne.get(i)){
                temp.set(i, true);
            }
        }
        return temp;
    }
    
    private void duplicateList(ArrayList<Boolean> old, ArrayList<Boolean> newOne){
        for(int i = 0; i< newOne.size(); i++){
            old.set(i, newOne.get(i));
        }
    }
}
