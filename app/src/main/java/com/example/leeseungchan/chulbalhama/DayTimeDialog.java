package com.example.leeseungchan.chulbalhama;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.leeseungchan.chulbalhama.UI.components.CustomDayCheckBox;
import com.example.leeseungchan.chulbalhama.UI.components.CustomSevenDayInfo;

import java.util.ArrayList;

public class DayTimeDialog {
    private Context context;

    public DayTimeDialog(Context context) {
        this.context = context;
    }

    public void callFunction(final ArrayList<Boolean> result,
                             final ArrayList<Integer> numbers,
                             final CustomSevenDayInfo sevenDayInfo) {

        final Dialog dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dlg.setContentView(R.layout.dialog_time_day);

        dlg.show();

        final CustomDayCheckBox dayCheckBox =
                new CustomDayCheckBox(dlg.findViewById(R.id.custom_days_checkbox));
        final TimePicker timePicker = (TimePicker) dlg.findViewById(R.id.time_picker);
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numbers.size() != 0) {
                    numbers.set(0, timePicker.getCurrentHour());
                }else{
                    numbers.add(timePicker.getCurrentHour());
                }

                if(numbers.size() == 2) {
                    numbers.set(1, timePicker.getCurrentHour());
                }else{
                    numbers.add(timePicker.getCurrentMinute());
                }
                dayCheckBox.getResult(result);
                sevenDayInfo.setWholeTimeRow(result, numbers);
                sevenDayInfo.pickDay(result);
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
}
