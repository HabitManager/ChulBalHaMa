/**
 * @todo implement drop down
 */
package com.example.leeseungchan.chulbalhama.UI.location_info;

import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.leeseungchan.chulbalhama.DBHelper;
import com.example.leeseungchan.chulbalhama.DayTimeDialog;
import com.example.leeseungchan.chulbalhama.Activities.LocationInfoActivity;
import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.UI.components.CustomSevenDayInfo;
import com.example.leeseungchan.chulbalhama.UI.map.MapAddFragment;

import java.util.ArrayList;

public class DestinationInfoFragment extends Fragment{

    private ArrayList<Boolean> days = new ArrayList<>();
    private ArrayList<Integer> time = new ArrayList<>();

    Bundle bundle = new Bundle();

    public static DestinationInfoFragment newInstance(Bundle bundle){
        DestinationInfoFragment v = new DestinationInfoFragment();
        v.bundle = bundle;
        return v;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_destination_info, container, false);

        bundle.putInt("type", 1);

        final EditText dest_name = v.findViewById(R.id.destination_name);
        String name = ((LocationInfoActivity)getActivity()).getName();
        if(name != null) {
            dest_name.setText(((LocationInfoActivity) getActivity()).getName());
        }
        setNameListener(dest_name);

        /* destination coordination view */
        LinearLayout destinationCord = v.findViewById(R.id.destination_setting);

        // destination TextView guide text
        final TextView destGuideText = destinationCord.findViewById(R.id.item_name);
        String address = bundle.getString("address");
        if(address == null)
            destGuideText.setText(R.string.guide_address);
        else
            destGuideText.setText(address);

        TextView destText = destinationCord.findViewById(R.id.item_description);
        destText.setVisibility(View.INVISIBLE);

        // set add button
        Button destSetButton = destinationCord.findViewById(R.id.button_change);
        destSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fg;
                fg = MapAddFragment.newInstance(bundle);
                if (!fg.isAdded()) {
                    transaction.replace(R.id.nav_host_fragment, fg)
                            .commitNowAllowingStateLoss();
                }
            }
        });
        destSetButton.setText(R.string.button_setting);

        Button destDelButton = destinationCord.findViewById(R.id.button_delete);
        destinationCord.removeView(destDelButton);


        /* time setting view */
        LinearLayout timeCord = v.findViewById(R.id.destination_time_duration);

        // time TextView guide text
        final TextView timeGuideText = timeCord.findViewById(R.id.item_name);
        final int time_hour = ((LocationInfoActivity) getActivity()).getTimeHour();
        int time_minute = ((LocationInfoActivity) getActivity()).getTimeMin();

        if(time_hour > 0 && time_minute > 0){
            timeGuideText.setText(time_hour + "시간 " + time_minute + "분");
        }else{
            timeGuideText.setText(R.string.guide_when_time);
        }

        TextView timeText = timeCord.findViewById(R.id.item_description);
        timeText.setVisibility(View.INVISIBLE);

        // set add button
        Button timeButton = timeCord.findViewById(R.id.button_change);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // @todo time-day dialog is needed.
                TimePickerDialog dialog = new TimePickerDialog(
                                getContext(),
                                android.R.style.Theme_Holo_Light_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ((LocationInfoActivity) getActivity()).setTimeHour(hourOfDay);
                        ((LocationInfoActivity) getActivity()).setTimeMin(minute);
                        timeGuideText.setText(hourOfDay + "시간 " + minute + "분");
                    }
                }, 0,0,true);


                dialog.show();

            }
        });
        timeButton.setText(R.string.button_setting);

        Button timeDelButton = timeCord.findViewById(R.id.button_delete);
        timeCord.removeView(timeDelButton);



        /* start time */

        // set 2d array for day and time
        LinearLayout dayInput = v.findViewById(R.id.day_input);
        final CustomSevenDayInfo sevenDayInfo = new CustomSevenDayInfo(dayInput);
        ArrayList<String> isTempData = ((LocationInfoActivity) getActivity()).getDayOfWeekTime();
        if(isTempData != null){
            sevenDayInfo.setTempTimeData(isTempData);
        }

        // set input layout
        LinearLayout destination = v.findViewById(R.id.destination_time_start);

        // time TextView guide text
        TextView endPointName = destination.findViewById(R.id.item_name);
        endPointName.setVisibility(View.INVISIBLE);

        TextView endPointDesc = destination.findViewById(R.id.item_description);
        endPointDesc.setVisibility(View.INVISIBLE);

        // set add button
        Button endPointChangeBtn = destination.findViewById(R.id.button_change);
        endPointChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayTimeDialog dayTimeDialog = new DayTimeDialog(getContext());
                dayTimeDialog.callFunction(days, time, sevenDayInfo);
                Toast.makeText(getContext(), "button clicked", Toast.LENGTH_SHORT).show();
            }
        });
        endPointChangeBtn.setText(R.string.button_setting);

        Button startTimeDel = destination.findViewById(R.id.button_delete);
        startTimeDel.setVisibility(View.GONE);
        destination.removeView(timeDelButton);


        /* destination store */
        Button destinationStoreBtn = v.findViewById(R.id.store_destination);
        destinationStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(v.getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // destination
                String sql = "insert into destinations (coordinate, time, destination_name) values(?,?,?)";

                String[] data = {
                        destGuideText.getText().toString(),
                        timeGuideText.getText().toString(),
                        dest_name.getText().toString()
                };
                db.execSQL(sql, new Object[]{data[0], data[1], data[2]});
                db.close();

                // day
                ArrayList<String> times = new ArrayList<>();
                sevenDayInfo.setTimeToTime(times);
                sevenDayInfo.storeTimeToDays(days,times);

                getActivity().finish();
            }
        });

        return v;
    }



    private void setNameListener(EditText edit){
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                ((LocationInfoActivity)getActivity()).setName(arg0.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });
    }

}