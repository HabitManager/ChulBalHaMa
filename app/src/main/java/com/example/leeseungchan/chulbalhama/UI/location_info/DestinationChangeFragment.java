/**
 * @todo implement drop down
 */
package com.example.leeseungchan.chulbalhama.UI.location_info;

import android.app.TimePickerDialog;
import android.database.Cursor;
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
import com.example.leeseungchan.chulbalhama.UI.components.CustomChangeDeleteItem;
import com.example.leeseungchan.chulbalhama.UI.components.CustomSevenDayInfo;
import com.example.leeseungchan.chulbalhama.UI.map.MapAddFragment;
import com.example.leeseungchan.chulbalhama.VO.DestinationsVO;
import com.example.leeseungchan.chulbalhama.VO.LocationVO;
import com.google.android.material.shape.CutCornerTreatment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DestinationChangeFragment extends Fragment{
    
    private ArrayList<Boolean> days = new ArrayList<>();
    private ArrayList<Integer> time = new ArrayList<>();
    private LocationVO locationVO;
    private CustomSevenDayInfo sevenDayInfo;
    private ArrayList<String> dayOfWeekTime;
    private View v;
    private String timeInput;
    
    Bundle bundle = new Bundle();
    
    public static DestinationChangeFragment newInstance(Bundle bundle){
        DestinationChangeFragment v = new DestinationChangeFragment();
        v.bundle = bundle;
        return v;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saveInstanceState) {
        v = inflater.inflate(R.layout.fragment_destination_info, container, false);
        int type = bundle.getInt("type",3);
        bundle.putInt("type", 3);
        locationVO = (LocationVO) bundle.getSerializable("locationVO");
        dayOfWeekTime = bundle.getStringArrayList("dayOfWeekTime");
        setDays();
        timeInput = bundle.getString("time");
        
        /* EditText to get name*/
        setEditTextText((EditText) v.findViewById(R.id.destination_name),locationVO);
        
        /* destination coordination view */
        setDestInfoChangeDeleteItem(v, R.id.destination_setting);
        
        
        /* time setting view */
        setDestInfoChangeDeleteItem(v, R.id.destination_time_duration);
        
        
        /* start time view */
        // set input layout
        setDestInfoChangeDeleteItem(v, R.id.destination_time_start);
        
        // set 2d array for day and time
        setDayOfWeekArray(v);
        
        
        /* destination store */
        updateDestination(v);
        
        return v;
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
    
    private void setDestInfoChangeDeleteItem(View v, int id){
        LinearLayout layout = v.findViewById(id);
        CustomChangeDeleteItem item = new CustomChangeDeleteItem(layout);
        // set title text view
        setTitle(id, item);
        // set description textView invisible
        item.setVisibility(item.DESCRIPTION, View.INVISIBLE);
        // set change button text & setOnClickListener
        item.setChange(getResources().getString(R.string.button_setting));
        setDestInfoClickListener(item.getChange(), layout);
        // set delete button visible.
        if(id == R.id.destination_time_start) {
            item.getDelete().setBackground(getResources().getDrawable(R.drawable.ic_refresh_black_24dp));
            item.getDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDays();
                    sevenDayInfo.pickDay(days);
                    refresh();
                }
            });
        }else
            item.setVisibility(item.DELETE_BTN, View.GONE);
    }
    
    private void refresh(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }
    
    private void setTitle(int id, CustomChangeDeleteItem item){
        switch (id){
            case R.id.destination_setting:
                String address = bundle.getString("address");
                setAddress(item, address);
                break;
            case R.id.destination_time_duration:
                setTime(item, locationVO);
                break;
            case R.id.destination_time_start:
                item.setVisibility(item.TITLE, View.GONE);
        }
    }
    
    private void setAddress(CustomChangeDeleteItem item, String address){
        if(address == null)
            item.setTitle("");
        else
            item.setTitle(address);
    }
    private void setTime(CustomChangeDeleteItem time, LocationVO locationVO){
        int time_hour = locationVO.getTimeHour();
        int time_minute = locationVO.getTimeMin();
        
        if(time_hour >= 0 && time_minute >= 0){
            time.setTitle(time_hour + "시간 " + time_minute+"분");
        }else{
            time.setTitle(timeInput);
        }
    }
    
    private void updateDestination(View v){
        Button destinationStoreBtn = v.findViewById(R.id.store_destination);
        destinationStoreBtn.setText(R.string.button_change);
        storeAndUpdateAndFinish(destinationStoreBtn);
    }
    
    
    
    private void setDestInfoClickListener(View target, final View root){
        target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (root.getId()){
                    case R.id.destination_setting:
                        callMapFragment();
                        break;
                    case R.id.destination_time_duration:
                        showTimeDialog();
                        break;
                    case R.id.destination_time_start:
                        DayTimeDialog dayTimeDialog = new DayTimeDialog(getContext(), days);
                        dayTimeDialog.callFunction(time, sevenDayInfo);
                        break;
                }
            }
        });
    }
    
    private void setDays(){
        if(days.size()!= 0)
            return;
        for(int i = 0; i < 7; i++){
            days.add(false);
        }
    }
    
    private void showTimeDialog(){
        TimePickerDialog dialog = new TimePickerDialog(
            getContext(),
            android.R.style.Theme_Holo_Light_Dialog,
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    setTime(hourOfDay, minute);
                }
            }, 0,0,true);
        
        
        dialog.show();
    }
    
    private void callMapFragment(){
        FragmentTransaction transaction =
            getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fg;
        fg = MapAddFragment.newInstance(bundle);
        if (!fg.isAdded()) {
            transaction.replace(R.id.nav_host_fragment, fg)
                .commitNowAllowingStateLoss();
        }
    }
    
    private void setDayOfWeekArray(View v){
        LinearLayout dayInput = v.findViewById(R.id.day_input);
        sevenDayInfo = new CustomSevenDayInfo(dayInput);
        ArrayList<String> isTempData = dayOfWeekTime;
        if(isTempData != null){
            sevenDayInfo.setSomeTimeRow(isTempData);
        }
        sevenDayInfo.pickDay(showDestination(locationVO.getId()));
    }
    
    private void storeAndUpdateAndFinish(View v){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    
                String time;
    
                if(locationVO.getTime()==null)
                    time = timeInput;
                else
                    time = locationVO.getTime();
                
                if(checkEvertThingInserted()) {
                    // insert destination data to sqlite db
                    updateDest(locationVO.getCoordinate(), time, locationVO.getName(), locationVO.getId());
                    
                    // update dayOfWeek table.
                    updateDayOfWeek();
                    
                    getActivity().finish();
                }
            }
        });
    }
    
    private void updateDest(String coordinate, String time, String name, int id){
        DBHelper dbHelper =  DBHelper.getInstance(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // destination
        String sql = "update destinations set coordinate=?, time=?, destination_name=? where _id=?";
        
        db.execSQL(sql, new Object[]{coordinate,time,name, id});
        db.close();
    }
    
    private void updateDayOfWeek(){
        ArrayList<String> times = new ArrayList<>();
        sevenDayInfo.getResultTimeDataInput(times);
        sevenDayInfo.updateTimeToDays(days,times);
    }
    
    private boolean checkEvertThingInserted(){
        if(!isDestNameEmpty() && !isMapUnselected()){
            return true;
        }
        return false;
    }
    
    private boolean isDestNameEmpty(){
        if(locationVO.getName() == null || locationVO.getName().length() == 0){
            Toast.makeText(getActivity().getApplicationContext(), "이름을 입력해 주시기 바랍니다.",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    
    private boolean isMapUnselected(){
        if(locationVO.getCoordinate() == null || locationVO.getCoordinate().length() == 0){
            Toast.makeText(getActivity().getApplicationContext(), "장소를 설정해 주시기 바랍니다.",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    
    private void setTime(int hour, int min){
        locationVO.setTime(hour, min);
        refresh();
    }
    
    private ArrayList<Boolean> showDestination(int destId){
        for(int i = 0; i < 7; i++){
            days.add(false);
        }
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        String sql = "select _id from day_of_week where destination_id = ? order by _id";
        Cursor c = db.rawQuery(sql, new String[]{Integer.toString(destId)});
        while(c.moveToNext()){
            int id = c.getInt(0);
            if(id > 7) {
                db.close();
                return days;
            }
            days.set(id-1, true);
        }
        db.close();
        return days;
    }
}