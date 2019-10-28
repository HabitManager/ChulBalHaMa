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

import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.UI.map.DestinationInfoFragment;

public class TestFragment extends Fragment implements View.OnClickListener{


    public static TestFragment newInstance(){
        return new TestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saveInstanceState){
        View v = inflater.inflate(R.layout.test_fragment, container, false);

        Button dbTestBtn = v.findViewById(R.id.test_db);
        dbTestBtn.setOnClickListener(this);
        Button mapTestBtn = v.findViewById(R.id.test_map);
        mapTestBtn.setOnClickListener(this);

        return v;
    }

    public void onClick(View view) {

        Fragment fg;
        switch (view.getId()) {
            case R.id.test_db:
                fg = DBTestFragment.newInstance();
                if(!fg.isAdded()) {
                    replaceFragment(fg);
                }
                break;
            case R.id.test_map:
                fg = MapTestFragment.newInstance();
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
