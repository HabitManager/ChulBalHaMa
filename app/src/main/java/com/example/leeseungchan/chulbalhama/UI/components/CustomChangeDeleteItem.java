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
    
    public final int TITLE       = 0;
    public final int DESCRIPTION = 1;
    public final int CHANGE_BTN  = 2;
    public final int DELETE_BTN  = 3;
    
    
    public CustomChangeDeleteItem(View v){
        title = v.findViewById(R.id.item_name);
        description = v.findViewById(R.id.item_description);
        change = v.findViewById(R.id.button_change);
        delete = v.findViewById(R.id.button_delete);
    }
    
    public void setTitle(String title) {
        this.title.setText(title);
    }
    
    public String getTitle(){
        return this.title.getText().toString();
    }
    
    public void setDescription(String description) {
        this.description.setText(description);
    }
    
    public Button getChange(){
        return change;
    }
    
    public void setChange(String  change) {
        this.change.setText(change);
    }
    
    public void setVisibility(int id, int visible){
        View v = getSelectedView(id);
        v.setVisibility(visible);
    }
    
    public void setDelete(Button delete) {
        this.delete = delete;
    }
    
    public View getSelectedView(int id){
        switch (id){
            case 0:
                return title;
            case 1:
                return description;
            case 2:
                return change;
            case 3:
                return delete;
        }
        return null;
    }
}
