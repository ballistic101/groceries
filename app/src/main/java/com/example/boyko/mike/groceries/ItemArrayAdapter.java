package com.example.boyko.mike.groceries;

import android.content.Context;
import android.graphics.Color;
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

    private LayoutInflater mInflater;


    public ItemArrayAdapter(Context context, int textViewResourceId) {
        this.mData = new ArrayList<Object>();

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Find the list of categories and add them in
        CategoryManager catMgr = CategoryManager.getInstance();
        ArrayList<Category> categories = catMgr.getCategories();

        for (Category category: categories) {
            addSectionHeaderItem(category);
        }
    }


    public void addItem(Item item) {

        Category category = item.category;

        if (category == null) {
            category = CategoryManager.getInstance().getUncategorized();
        }

        boolean added = false;
        for (int i=0; i<mData.size(); i++) {
            Object obj = mData.get(i);

            if (obj instanceof Category) {
                if (((Category)obj).id == category.id) {
                    mData.add(i+1, item);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            if (category == null) {
                Log.e("Groceries", "Somehow category is null in addItem!");
                return;
            }
            else {
                Log.e("Groceries", "Somehow category is illegal in addItem!");
                return;
            }
        }
        //mData.add(item);
        notifyDataSetChanged();
    }


    public void addSectionHeaderItem(Category item) {
        mData.add(item);
        notifyDataSetChanged();
    }


    /**
     * When updating an item it is best to re-add it again if
     * the category changed.
     * @param position
     * @param item
     */
    public void updateItem(int position, Item item) {

        Object obj = mData.get(position);

        if (obj == null) {
            // For some reason there was no object at that position, so just add it.
            addItem(item);
            return;
        }

        if (obj instanceof Category) {
            // For some reason that position is occupied by a Category.
            // Just add the item.
            addItem(item);
            return;
        }

        if (! categoriesEqual(item.category, ((Item)obj).category)) {
            // The category changed. Remove the item from the list and re-add it.
            mData.remove(position);
            addItem(item);
            return;
        }

        // The item changed, but the category did not. Just replace it in-situ.
        mData.set(position, item);
        notifyDataSetChanged();
    }


    public void deleteItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return mData.get(position) instanceof Category ? TYPE_CATEGORY : TYPE_ITEM;
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


    /**
     * A specific kind of get method which checks
     * if the Object at that position is a category or not.
     * If it is a category then it will return null. Otherwise
     * the object is returned.
     *
     * @param position The position in the Item ArrayList.
     * @return null if not found or the object is a CategoryHeader. An Item otherwise.
     */
    public Item getListItem(int position) {
        if (getItemViewType(position) == TYPE_CATEGORY) {
            return null;
        }
        return (Item)getItem(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Compare two categories to see if they are the same.
     * @param c1 Category1
     * @param c2 Category2
     * @return true if the categories are the same, or both are null.
     */
    private boolean categoriesEqual(Category c1, Category c2) {
        if (c1 == null && c2 != null) {
            return false;
        }
        else if (c1 != null && c2 == null) {
            return false;
        }
        else if (c1 == null && c2 == null) {
            return true;
        }

        return c1.id == c2.id;
    }


    private class ViewHolder {
        TextView item;
        CheckBox checked;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        int rowType = getItemViewType(position);

        if (convertView == null) {

            holder = new ViewHolder();

            switch (rowType) {
                case TYPE_CATEGORY:
                    convertView = mInflater.inflate(R.layout.custom_item_list_header_layout, null);
                    holder.item = (TextView) convertView.findViewById(R.id.textCategoryHeader);
                    break;
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.custom_item_list_layout, null);
                    holder.item = (TextView) convertView.findViewById(R.id.item);
                    holder.checked = (CheckBox) convertView.findViewById(R.id.checkBox1);

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

                    break;
            }

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (rowType) {
            case TYPE_CATEGORY:
                Category category = (Category) mData.get(position);
                holder.item.setText(category.toString());
                holder.item.setBackgroundColor(Color.parseColor(category.color));
                break;
            case TYPE_ITEM:
                Item item = (Item) mData.get(position);
                holder.item.setText(item.toString());
                holder.checked.setChecked(item.checked);
                holder.checked.setTag(item);
                break;
        }

        return convertView;
    }

}

