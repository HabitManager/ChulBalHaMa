package com.example.leeseungchan.chulbalhama.Adpater;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.Activities.LocationInfoActivity;
import com.example.leeseungchan.chulbalhama.DBHelper;
import com.example.leeseungchan.chulbalhama.R;
import com.example.leeseungchan.chulbalhama.VO.DestinationsVO;
import com.example.leeseungchan.chulbalhama.VO.LocationVO;

import java.util.ArrayList;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ListViewHolder>{

    private ArrayList<DestinationsVO> mData = null;

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView item_name ;
        Button change, delete;


        ListViewHolder(final View itemView) {
            super(itemView);
    
            item_name = itemView.findViewById(R.id.item_name);
            change = itemView.findViewById(R.id.button_change);
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), LocationInfoActivity.class);
                    intent.putExtra("type", 3);
                    intent.putExtra("locationVO", getLocationByDest(mData.get(getAdapterPosition())));
                    intent.putExtra("time", mData.get(getAdapterPosition()).getTime());
                    itemView.getContext().startActivity(intent);
                }
            });
            delete = itemView.findViewById(R.id.button_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = getAdapterPosition();
                    int destId = mData.get(id).getId();
                    deleteList(id);
                    notifyDataSetChanged();
                    DBHelper dbHelper = DBHelper.getInstance(itemView.getContext());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    String time = "update day_of_week " +
                                  "set departure_time=null, habit_id = null, destination_id = null " +
                                  "where destination_id="
                        + destId;
                    db.execSQL(time);
                    String sql = "delete from destinations where _id=" + destId;
                    db.execSQL(sql);
                    db.close();
                }
            });
        }
        
        public void setItem_name(String name){
            item_name.setText(name);
        }
    }
    
    

    public DestinationAdapter(ArrayList<DestinationsVO> list) {
        mData = list ;
    }
    public DestinationAdapter(){}

    @Override
    public DestinationAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater =
            (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_list_change_delete, parent, false);
        DestinationAdapter.ListViewHolder vh = new DestinationAdapter.ListViewHolder(view);

        return vh ;
    }

    @Override
    public void onBindViewHolder(DestinationAdapter.ListViewHolder holder, int position) {
        DestinationsVO destination = mData.get(position) ;
        holder.setItem_name(destination.getDestinationName());
    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        return mData.size() ;
    }


    public void addList(DestinationsVO name){
        mData.add(name);
    }

    public void deleteList(int position){
        mData.remove(position);
    }
    
    private LocationVO getLocationByDest(DestinationsVO destinationsVO){
        LocationVO locationVO = new LocationVO();
        locationVO.setName(destinationsVO.getDestinationName());
        locationVO.setDescription(destinationsVO.getCoordinate());
        locationVO.setId(destinationsVO.getId());
        return locationVO;
    }
}
