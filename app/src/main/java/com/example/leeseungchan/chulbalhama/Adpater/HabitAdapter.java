package com.example.leeseungchan.chulbalhama.Adpater;


import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.Activities.HabitInfoActivity;
import com.example.leeseungchan.chulbalhama.DBHelper;
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
        private LinearLayout linearLayout;
        private CheckBox showAdditional;
        private View view;
        private HabitViewHolder holder;
        private boolean isChecked = true;

        public HabitViewHolder(@NonNull final View v){
            super(v);
            this.view = v;
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
    
            linearLayout = v.findViewById(R.id.list_habit);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isChecked){
                        habitInfo.setVisibility(View.VISIBLE);
                        habitHistory.setVisibility(View.VISIBLE);
                        habitDelete.setVisibility(View.VISIBLE);
                        isChecked = false;
                        showAdditional.setChecked(true);
                    }else{
                        habitInfo.setVisibility(View.GONE);
                        habitHistory.setVisibility(View.GONE);
                        habitDelete.setVisibility(View.GONE);
                        isChecked = true;
                        showAdditional.setChecked(false);
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
                    deleteHabit(getAdapterPosition(), view.getContext());
                    notifyDataSetChanged();
                }
            });
        }

        public void setHabit(HabitsVO itemHabit){
            habitName.setText(itemHabit.getHabitName());
            String description = itemHabit.getDue() + "일";
            habitDescription.setText(description);
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

 
    private HabitsVO getHabit(int position){
        return mDataSet.get(position);
    }
    
    private void startIntent(int type, HabitViewHolder holder){
        Intent intent = new Intent(holder.view.getContext(), HabitInfoActivity.class);
        HabitsVO habit = getHabit(holder.getAdapterPosition());
        intent.putExtra("habit", habit);
        intent.putExtra("type", type);
        holder.view.getContext().startActivity(intent);
    }
    private void deleteHabit(int position, Context context){
        // delete on db
        int id = mDataSet.get(position).getId();
        deleteOnHabitTable(id, context);
    
        // delete on list
        mDataSet.remove(position);
    }
    private void deleteOnHabitTable(int id, Context context){
        DBHelper dbHelper =  DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "delete from habits where _id = ?";
        db.execSQL(sql, new Object[]{id});
        db.close();
    }
}
