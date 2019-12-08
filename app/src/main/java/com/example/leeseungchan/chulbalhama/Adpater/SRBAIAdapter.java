package com.example.leeseungchan.chulbalhama.Adpater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.VO.SrbaiVO;

import java.util.ArrayList;

public class SRBAIAdapter extends RecyclerView.Adapter<SRBAIAdapter.SRBAIViewHolder> {
    
    private ArrayList<SrbaiVO> mData;
    
    public class SRBAIViewHolder extends RecyclerView.ViewHolder {
        private TextView score, date;
        public SRBAIViewHolder (@NonNull View v){
            super(v);
            score = v.findViewById(R.id.score);
            date = v.findViewById(R.id.date);
        }
        public void setSrbai(int score, String day){
            date.setText(day);
            this.score.setText(Integer.toString(score));
        }
    }
    
    public SRBAIAdapter(){};
    public SRBAIAdapter(ArrayList<SrbaiVO> data){
        mData = data;
    }
    
    @Override
    public SRBAIAdapter.SRBAIViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType){
        
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_history, parent, false);
        SRBAIViewHolder vh = new SRBAIViewHolder(v);
        return vh;
    }
    
    @Override
    public void onBindViewHolder(SRBAIViewHolder holder, int position){
        SrbaiVO srbaiVO = mData.get(position);
        int score = srbaiVO.getScore();
        String day = srbaiVO.getDay();
        holder.setSrbai(score, day);
    }
    
    public int getItemCount(){
        return mData.size();
    }
}