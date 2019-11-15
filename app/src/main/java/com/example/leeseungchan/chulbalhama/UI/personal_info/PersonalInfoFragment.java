package com.example.leeseungchan.chulbalhama.UI.personal_info;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import com.example.leeseungchan.chulbalhama.Adpater.DestinationAdapter;
import com.example.leeseungchan.chulbalhama.DBHelper;
import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.Activities.LocationInfoActivity;
import com.example.leeseungchan.chulbalhama.VO.DestinationsVO;
import com.example.leeseungchan.chulbalhama.VO.UserVO;

import java.util.ArrayList;

public class PersonalInfoFragment extends Fragment{

    private ArrayList<DestinationsVO> destinations = new ArrayList<>();
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal_info, container, false);
        dbHelper = new DBHelper(getContext());
        
        UserVO userVO = getUserVO();
        
        /* name */
        LinearLayout name = v.findViewById(R.id.info_name);
        
        TextView textName = name.findViewById(R.id.item_name);
        textName.setText(userVO.getName());

        TextView nameDesc = name.findViewById(R.id.item_description);
        nameDesc.setVisibility(View.INVISIBLE);

        Button nameChangeBtn = name.findViewById(R.id.button_change);
        nameChangeBtn.setText(R.string.button_change);
        nameChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNameDialog();
            }
        });

        Button nameDeleteBtn = name.findViewById(R.id.button_delete);
        nameDeleteBtn.setVisibility(View.GONE);
        name.removeView(nameDeleteBtn);


        /* start point */
        LinearLayout startPoint = v.findViewById(R.id.info_start);

        TextView startPointName = startPoint.findViewById(R.id.item_name);
        startPointName.setText(userVO.getStartingCoordinate());

        TextView startPointDesc = startPoint.findViewById(R.id.item_description);
        startPointDesc.setEnabled(false);
        startPointDesc.setText(userVO.getStartingCoordinate());

        Button startPointChangeBtn = startPoint.findViewById(R.id.button_change);
        startPointChangeBtn.setText(R.string.button_change);
        startPointChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LocationInfoActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });

        Button startPointDeleteBtn = startPoint.findViewById(R.id.button_delete);
        startPointDeleteBtn.setVisibility(View.GONE);
        startPoint.removeView(startPointDeleteBtn);


        /* destination */
        LinearLayout destination = v.findViewById(R.id.info_destination);
        Button endPointChangeBtn = destination.findViewById(R.id.button_for_selection);
        endPointChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LocationInfoActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });

        TextView endPointName = destination.findViewById(R.id.guide_for_selection);
        endPointName.setText(R.string.setting_destination);

        EditText endPointDesc = destination.findViewById(R.id.input_for_selection);
        endPointDesc.setVisibility(View.INVISIBLE);
        destination.removeView(endPointDesc);

        RecyclerView destinationRecycler = destination.findViewById(R.id.list);
        RecyclerView.LayoutManager layoutManager =
            new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
            );
        destinationRecycler.setLayoutManager(layoutManager);

        RecyclerView.Adapter mAdapter;
        mAdapter = new DestinationAdapter(destinations);
        destinationRecycler.setAdapter(mAdapter);

        retrieve();

        return v;
    }

    public void retrieve(){
        destinations.clear();
        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select destination_name from destinations";
        Cursor c = db.rawQuery(sql, null);
        while(c.moveToNext()){
            String name = c.getString(0);

            DestinationsVO h = new DestinationsVO();
            h.setDestinationName(name);

            destinations.add(h);
        }
        Log.e("destination size ", destinations.size() + "개");
    }

    public void setNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Title");
        View viewInflated =
                LayoutInflater.from(getContext())
                        .inflate(R.layout.dialog_edit_text, (ViewGroup) getView(), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String newName = input.getText().toString();
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                db.execSQL("update user set name=\""+ newName + "\" where _id=1");
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
    
    private UserVO getUserVO(){
        dbHelper = new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
    
        Cursor cursor =  db.rawQuery("select * from user order by _id", null);
        cursor.moveToNext();
        
        UserVO userVO = new UserVO(
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3)
        );
        db.close();
        return userVO;
    }
}
