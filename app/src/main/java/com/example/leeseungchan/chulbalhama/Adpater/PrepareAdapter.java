package com.example.leeseungchan.chulbalhama.Adpater;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.R;

import java.util.ArrayList;

public class PrepareAdapter extends RecyclerView.Adapter<PrepareAdapter.ListViewHolder> {

    private ArrayList<String> mData = null;

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView item_name ;
        Button change, delete;


        ListViewHolder(View itemView) {
            super(itemView) ;

            item_name = itemView.findViewById(R.id.item_name) ;
            change = itemView.findViewById(R.id.button_change);
            change.setVisibility(View.INVISIBLE);
            delete = itemView.findViewById(R.id.button_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("\n"+mData.get(getAdapterPosition()));
                    deleteList(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }

    public PrepareAdapter(ArrayList<String> list) {
        mData = list ;
    }
    public PrepareAdapter(){}

    @Override
    public PrepareAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_list_change_delete, parent, false) ;
        PrepareAdapter.ListViewHolder vh = new PrepareAdapter.ListViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(PrepareAdapter.ListViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.item_name.setText(text) ;
    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        return mData.size() ;
    }

    public void addList(String name){
        mData.add(name);
    }

    public void deleteList(int position){
        mData.remove(position);
    }
}
