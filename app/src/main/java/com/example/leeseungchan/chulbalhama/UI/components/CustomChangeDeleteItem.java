package com.example.leeseungchan.chulbalhama.UI.components;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.leeseungchan.chulbalhama.R;

public class CustomChangeDeleteItem {
    
    private TextView title;
    private TextView description;
    private Button change;
    private Button delete;
    
    public CustomChangeDeleteItem(View v){
        title = v.findViewById(R.id.item_name);
        description = v.findViewById(R.id.item_description);
        change = v.findViewById(R.id.button_change);
        delete = v.findViewById(R.id.button_delete);
    }
    
    public TextView getTitle() {
        return title;
    }
    
    public void setTitle(TextView title) {
        this.title = title;
    }
    
    public TextView getDescription() {
        return description;
    }
    
    public void setDescription(TextView description) {
        this.description = description;
    }
    
    public Button getChange() {
        return change;
    }
    
    public void setChange(Button change) {
        this.change = change;
    }
    
    public Button getDelete() {
        return delete;
    }
    
    public void setDelete(Button delete) {
        this.delete = delete;
    }
    
}
