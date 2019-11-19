package com.example.leeseungchan.chulbalhama.Adpater;


import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.Activities.HabitInfoActivity;
import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.UI.habit.HabitHistoryFragment;
import com.example.leeseungchan.chulbalhama.VO.HabitsVO;

import java.util.ArrayList;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private ArrayList<HabitsVO> mDataSet = new ArrayList<>();

    // ViewHolder
    public class HabitViewHolder extends RecyclerView.ViewHolder{
        public TextView habitName;
        public TextView habitDescription;
        private TextView habitInfo, habitHistory, habitDelete;
        private CheckBox showAdditional;
        private View v;
        private HabitViewHolder holder;

        public HabitViewHolder(@NonNull final View v){
            super(v);
            this.v = v;
            holder = this;
            habitName = v.findViewById(R.id.item_habit_name);
            habitDescription = v.findViewById(R.id.item_habit_description);
            
            showAdditional = v.findViewById(R.id.show_additional);
            showAdditional.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        habitInfo.setVisibility(View.VISIBLE);
                        habitHistory.setVisibility(View.VISIBLE);
                        habitDelete.setVisibility(View.VISIBLE);
                    }else{
                        habitInfo.setVisibility(View.GONE);
                        habitHistory.setVisibility(View.GONE);
                        habitDelete.setVisibility(View.GONE);
                    }
                }
            });
            
            habitInfo = v.findViewById(R.id.info);
            habitInfo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    startIntent(0, holder);
                }
            });
            
            habitHistory = v.findViewById(R.id.history);
            habitHistory.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    startIntent(1, holder);
                }
            });
            habitDelete = v.findViewById(R.id.delete);
            habitDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
            
                }
            });
        }

        public void setHabit(HabitsVO itemHabit){
            habitName.setText(itemHabit.getHabitName());
            habitDescription.setText(itemHabit.getDescription());
        }
    }

    public HabitAdapter(){
        // empty constructor
    }
    public HabitAdapter(ArrayList<HabitsVO> items){
        mDataSet = items;
    }

    public void addHabit(HabitsVO itemHabit){
        mDataSet.add(itemHabit);
    }

    @Override
    public HabitAdapter.HabitViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType){

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_habit, parent, false);
        HabitViewHolder vh = new HabitViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(HabitViewHolder holder, int position){
        HabitsVO item = mDataSet.get(position);
        holder.setHabit(item);
    }

    @Override
    public int getItemCount(){
        return mDataSet.size();
    }

    private void deleteList(int position){
        int id = mDataSet.get(position).getId();
        //@todo delete thing
        mDataSet.remove(position);
    }

    private HabitsVO getHabit(int position){
        return mDataSet.get(position);
    }
    
    private void startIntent(int type, HabitViewHolder holder){
        Intent intent = new Intent(holder.v.getContext(), HabitInfoActivity.class);
        HabitsVO habit = getHabit(holder.getAdapterPosition());
        intent.putExtra("habit", habit);
        intent.putExtra("type", type);
        holder.v.getContext().startActivity(intent);
    }
}
