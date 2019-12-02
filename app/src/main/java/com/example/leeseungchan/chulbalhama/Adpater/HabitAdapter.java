package com.example.leeseungchan.chulbalhama.Adpater;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeseungchan.chulbalhama.Activities.HabitInfoActivity;
import com.example.leeseungchan.chulbalhama.DBHelper;
import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.UI.habit.HabitHistoryFragment;
import com.example.leeseungchan.chulbalhama.VO.HabitsVO;
import com.example.leeseungchan.chulbalhama.VO.SrbaiVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private ArrayList<HabitsVO> mDataSet = new ArrayList<>();
    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;
    
    // ViewHolder
    public class HabitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView habitName;
        public TextView habitDescription;
        private TextView habitInfo, srbaiGuide, habitHistory, habitDelete;
        private LinearLayout linearLayout;
        private View view;
        private HabitViewHolder holder;
        private int position;
        private Switch toggleBtn;

        public HabitViewHolder(@NonNull final View v){
            super(v);
            this.view = v;
            holder = this;
            habitName = v.findViewById(R.id.item_habit_name);
            habitDescription = v.findViewById(R.id.item_habit_description);
            
            linearLayout = v.findViewById(R.id.list_habit);
            habitInfo = v.findViewById(R.id.info);
            habitHistory = v.findViewById(R.id.history);
            habitDelete = v.findViewById(R.id.delete);
            srbaiGuide = v.findViewById(R.id.guide_srbai_test);
            toggleBtn = v. findViewById(R.id.toggle_btn);
            
            linearLayout.setOnClickListener(this);
            habitInfo.setOnClickListener(this);
            habitHistory.setOnClickListener(this);
            habitDelete.setOnClickListener(this);
            toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    HabitsVO habitsVO = (HabitsVO)mDataSet.get(getAdapterPosition());
                    if(isChecked)
                        habitsVO.setActive(1);
                    else
                        habitsVO.setActive(0);
                    setToggle(habitsVO);
                }
            });
            
        }

        public void setHabit(HabitsVO itemHabit){
            habitName.setText(itemHabit.getHabitName());
            String description = itemHabit.getDue() + "일";
            habitDescription.setText(description);
            
            if(isSrbaiFinished(itemHabit.getId()))
                srbaiGuide.setText("이미 설문을 진행하셨습니다.");
            else
                srbaiGuide.setText("srbai설문을 진행해 주세요!");
    
    
            if(!isValid(itemHabit.getId())){
                toggleBtn.setChecked(false);
                toggleBtn.setEnabled(false);
            }else {
                if (itemHabit.getActive() == 0) {
                    toggleBtn.setChecked(false);
                } else {
                    toggleBtn.setChecked(true);
                }
            }
        }
        
        void onBind(int position){
            this.position = position;
    
            changeVisibility(selectedItems.get(position));
        }
        
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.list_habit:
                    // opened Item clicked
                    if (selectedItems.get(position)) {
                        selectedItems.delete(position);
                    } else { // other Item clicked
                        selectedItems.delete(prePosition);
                        selectedItems.put(position, true);
                    }
                    
                    // notify change
                    if (prePosition != -1)
                        notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    
                    // set clicked position
                    prePosition = position;
                    break;
                case R.id.info:
                    startIntent(0, holder);
                    break;
                case R.id.history:
                    startIntent(1, holder);
                    break;
                case R.id.delete:
                    deleteHabit(getAdapterPosition(), view.getContext());
                    notifyDataSetChanged();
                    break;
            }
        }
    
        private void changeVisibility(final boolean isExpanded) {
            int dpValue = 150;
            float d = view.getContext().getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);
        
            // ValueAnimator.ofInt(int... values) set which to/from
            ValueAnimator va;
            if(isExpanded)
                va = ValueAnimator.ofInt(0, height);
            else
                va = ValueAnimator.ofInt(height, 0);
           
            // 0.6 sec
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if(isExpanded){
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
            // Animation start
            va.start();
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
        holder.onBind(position);
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
    
    private boolean isSrbaiFinished(int habitId){
        DBHelper dbHelper = DBHelper.getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select day from srbai where habit_id=? order by _id DESC limit 1";
        Cursor c = db.rawQuery(sql, new String[]{Integer.toString(habitId)});
        c.moveToNext();
        if(c.getCount() != 0) {
            String today = c.getString(0);
            if (today == null)
                return false;
    
            if (today.equals(getDate()))
                return true;
            else
                return false;
        }
        return false;
    }
    private String getDate(){
        String today;
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat date = new SimpleDateFormat("MM-dd",Locale.getDefault());
        today = date.format(currentTime);
        return today;
    }
    public void setToggle(HabitsVO habitsVO){
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        String sql = "update habits set active=? where _id=?";
        db.execSQL(sql, new Object[]{habitsVO.getActive(), habitsVO.getId()});
        db.close();
    }
    
    private boolean isValid(int id){
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        String sql = "select * from day_of_week where habit_id=?";
        Cursor c = db.rawQuery(sql, new String[]{Integer.toString(id)});
        int count = c.getCount();
        db.close();
        if(count == 0) {
            return false;
        }
        return true;
    }
}
