package com.example.boyko.mike.groceries;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeSet;


public class ItemArrayAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_CATEGORY = 1;

    private ArrayList<Object> mData;
    private TreeSet<Integer> categoryHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;


    public ItemArrayAdapter(Context context, int textViewResourceId) {
        this.mData = new ArrayList<Object>();

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public void addItem(Item item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        categoryHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }


    public void updateItem(int position, Item item) {
        mData.set(position, item);
        notifyDataSetChanged();
    }


    public void deleteItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return categoryHeader.contains(position) ? TYPE_CATEGORY : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
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
            convertView = mInflater.inflate(R.layout.custom_item_list_layout, null);

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

        Item item = (Item) mData.get(position);
        holder.item.setText(item.toString());
        holder.checked.setChecked(item.checked);
        // holder.checked.setText(item.toString());
        holder.checked.setTag(item);

        return convertView;
    }

}

