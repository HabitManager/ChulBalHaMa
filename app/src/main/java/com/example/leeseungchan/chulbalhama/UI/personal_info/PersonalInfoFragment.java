package com.example.leeseungchan.chulbalhama.UI.personal_info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.UI.map.DestinationInfoFragment;
import com.example.leeseungchan.chulbalhama.UI.map.MapAddFragment;

public class PersonalInfoFragment extends Fragment implements View.OnClickListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saveInstanceState){
        View v = inflater.inflate(R.layout.fragment_personal_info, container, false);

        Button startPointChangeBtn = v.findViewById(R.id.info_setting_start);
        startPointChangeBtn.setOnClickListener(this);
        Button endPointAddBtn = v.findViewById(R.id.info_add_destination);
        endPointAddBtn.setOnClickListener(this);

        fragmentManager = getActivity().getSupportFragmentManager();

        return v;
    }

    @Override
    public void onClick(View view) {

        Fragment fg;
        switch (view.getId()) {
        case R.id.info_setting_start:
            fg = DestinationInfoFragment.newInstance();
            if(!fg.isAdded()) {
                replaceFragment(fg);
            }
            break;
        case R.id.info_add_destination:
            fg = DestinationInfoFragment.newInstance();
            if(!fg.isAdded()) {
                replaceFragment(fg);
            }
            break;
        }
    }



    private void replaceFragment(Fragment fragment){
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment)
                .commitNowAllowingStateLoss();
    }
}
