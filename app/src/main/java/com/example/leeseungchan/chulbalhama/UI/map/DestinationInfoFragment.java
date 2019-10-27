/**
 * @todo implement drop down
 */

package com.example.leeseungchan.chulbalhama.UI.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.leeseungchan.chulbalhama.R;
 import com.example.leeseungchan.chulbalhama.UI.map.MapAddFragment;
 import com.example.leeseungchan.chulbalhama.UI.personal_info.PersonalInfoFragment;

public class DestinationInfoFragment extends Fragment implements View.OnClickListener {

    public static DestinationInfoFragment newInstance() {
        return new DestinationInfoFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_destination_info, container, false);

        Button destinationSetBtn = v.findViewById(R.id.destination_setting);
        destinationSetBtn.setOnClickListener(this);

        Button destinationStoreBtn = v.findViewById(R.id.destination_store);
        destinationStoreBtn.setOnClickListener(this);

//        Spinner hour = (Spinner)v.findViewById(R.id.hour);
//        hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });
//
//        Spinner minute = (Spinner)v.findViewById(R.id.min);
//        minute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });

        return v;
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fg;
        if (view.getId() == R.id.destination_setting) {
            fg = new MapAddFragment();
            if (!fg.isAdded()) {
                transaction.replace(R.id.nav_host_fragment, fg)
                        .commitNowAllowingStateLoss();
            }
        } else if (view.getId() == R.id.destination_store) {
            fg = new PersonalInfoFragment();
            if (!fg.isAdded()) {
                transaction.replace(R.id.nav_host_fragment, fg)
                        .commitNowAllowingStateLoss();
            }
        }
    }
}
