package com.example.leeseungchan.chulbalhama.UI.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.UI.map.DestinationInfoFragment;

public class MapTestFragment extends Fragment implements View.OnClickListener {


    public static MapTestFragment newInstance(){
        return new MapTestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saveInstanceState){
        View v = inflater.inflate(R.layout.test_fragment_map, container, false);

        Button backBtn = v.findViewById(R.id.back);
        backBtn.setOnClickListener(this);
        return v;
    }

    public void onClick(View view) {

        Fragment fg;
        switch (view.getId()) {
            case R.id.back:
                fg = TestFragment.newInstance();
                if(!fg.isAdded()) {
                    replaceFragment(fg);
                }
                break;
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment)
                .commitNowAllowingStateLoss();
    }

}
