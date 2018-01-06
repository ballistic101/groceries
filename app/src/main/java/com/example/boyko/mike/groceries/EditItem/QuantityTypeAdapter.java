package com.example.boyko.mike.groceries.EditItem;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.boyko.mike.groceries.R;
import com.example.boyko.mike.groceries.db.models.Category;
import com.example.boyko.mike.groceries.db.models.ListItem;
import com.example.boyko.mike.groceries.db.models.QuantityType;

import java.util.ArrayList;
import java.util.List;


public class QuantityTypeAdapter extends ArrayAdapter {

    // This is the list of Quantity Types
    private List<QuantityType> types;
    int id;

    private LayoutInflater mInflater;


    public QuantityTypeAdapter(Context context, int spinnerResourceId) {
        super(context, spinnerResourceId);

        types = new ArrayList<QuantityType>();

        // Dump an empty array in to start
        addAll(types);

        /*
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        */
    }


    public void setQuantityTypes(List<QuantityType> types) {
        this.types = types;
        clear();
        addAll(types);
        notifyDataSetChanged();
    }


    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            convertView = mInflater.inflate();
        }

        QuantityType type = types.get(position);
        holder.item.setText(listItem.toString());
        holder.checked.setChecked(listItem.checked);
        holder.checked.setTag(listItem);

        return convertView;
    }
    */

}

