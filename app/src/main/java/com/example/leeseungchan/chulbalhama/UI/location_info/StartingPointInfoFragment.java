package com.example.leeseungchan.chulbalhama.UI.location_info;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.DBHelper;
import com.example.leeseungchan.chulbalhama.Activities.LocationInfoActivity;
import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.UI.map.MapAddFragment;
import com.example.leeseungchan.chulbalhama.VO.LocationVO;

import java.util.ArrayList;

public class StartingPointInfoFragment extends Fragment {
    Bundle bundle = new Bundle();

    public static StartingPointInfoFragment newInstance(Bundle bundle){
        StartingPointInfoFragment v = new StartingPointInfoFragment();
        v.bundle = bundle;
        return v;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_starting_info, container, false);

        bundle.putInt("type", 0);
        final LocationVO locationVO = (LocationVO) bundle.getSerializable("locationVO");
        if(locationVO == null){
            Log.e("location VO error ", "null exception");
        }
        final EditText start_name = v.findViewById(R.id.starting_name);
   
        String name = locationVO.getName();

        if(name != null) {
            start_name.setText(name);
        }
        setNameListener(start_name, locationVO);

        /* starting setting view */
        LinearLayout destinationCord = v.findViewById(R.id.starting_setting);

        // starting TextView guide text
        final TextView destGuideText = destinationCord.findViewById(R.id.item_name);
        String address = bundle.getString("address");
        if(address == null)
            destGuideText.setText(R.string.guide_address);
        else
            destGuideText.setText(address);

        TextView startText = destinationCord.findViewById(R.id.item_description);
        startText.setVisibility(View.INVISIBLE);

        // set add button
        Button startSetButton = destinationCord.findViewById(R.id.button_change);
        startSetButton.setOnClickListener(new View.OnClickListener() {
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
        startSetButton.setText(R.string.button_setting);

        Button startingPoint = destinationCord.findViewById(R.id.button_delete);
        destinationCord.removeView(startingPoint);


        /* starting store */
        Button startStoreBtn = v.findViewById(R.id.store_starting);
        startStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(v.getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String sql = "update user set starting_name=\""+start_name.getText().toString() +
                        "\", starting_coordinate=\""+destGuideText.getText().toString() + "\" where _id=1";
                db.execSQL(sql);
                db.close();
                getActivity().finish();
            }
        });

        return v;
    }

    private void setNameListener(EditText edit, final LocationVO locationVO){
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                locationVO.setName(arg0.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });
    }
}