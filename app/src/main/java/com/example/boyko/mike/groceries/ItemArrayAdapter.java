package com.example.boyko.mike.groceries;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ItemArrayAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> itemList;
    private Activity activity;

    public ItemArrayAdapter(Context context, int textViewResourceId,
                            ArrayList<Item> itemList, Activity activity) {
        super(context, textViewResourceId, itemList);
        this.itemList = itemList;
        this.activity = activity;
    }

    private class ViewHolder {
        TextView item;
        CheckBox checked;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)activity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.custom_item_list_layout, null);

            holder = new ViewHolder();
            holder.item = (TextView) convertView.findViewById(R.id.item);
            holder.checked = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);

            holder.checked.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    Item item = (Item) cb.getTag();
                    Log.v("ConvertView","Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked());
                    item.checked = cb.isChecked();

                    ViewParent parent = v.getParent();

                    if (parent != null) {
                        ViewGroup parentGroup = (ViewGroup)parent;
                        TextView tv = (TextView) parentGroup.findViewById(R.id.item);

                        if (item.checked) {
                            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }
                        else {
                            tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        }
                        notifyDataSetChanged();
                    }

                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = itemList.get(position);
        holder.item.setText(item.toString());
        holder.checked.setChecked(item.checked);
        // holder.checked.setText(item.toString());
        holder.checked.setTag(item);

        return convertView;
    }

}

