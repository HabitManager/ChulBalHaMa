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

public class MapAddFragment extends Fragment implements View.OnClickListener{


    public static MapAddFragment newInstance(){
        return new MapAddFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saveInstanceState){
        View v = inflater.inflate(R.layout.fragment_map_select, container, false);

        Button registerLocation = v.findViewById(R.id.register_btn);

        return v;
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fg;
        if(view.getId() == R.id.register_btn){
            // @todo get the data and pass to the information fragment.
            fg = new DestinationInfoFragment();
            if(!fg.isAdded()) {
                transaction.replace(R.id.nav_host_fragment, fg)
                        .commitNowAllowingStateLoss();
            }
        }
    }

}
