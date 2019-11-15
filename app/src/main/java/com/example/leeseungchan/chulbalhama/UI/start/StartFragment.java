package com.example.leeseungchan.chulbalhama.UI.start;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.leeseungchan.chulbalhama.Activities.LocationInfoActivity;
import com.example.leeseungchan.chulbalhama.R;

public class StartFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first_main, container, false);

        Button starting = v.findViewById(R.id.button_for_start);
        starting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_location(0);
            }
        });
        Button dest = v.findViewById(R.id.button_for_dest);
        dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_location(1);
            }
        });
        Button store = v.findViewById(R.id.store);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return v;
    }
    private void start_location(int type){
        Intent intent = new Intent(getContext(), LocationInfoActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }


}
