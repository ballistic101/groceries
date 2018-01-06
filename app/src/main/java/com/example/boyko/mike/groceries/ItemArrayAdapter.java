package com.example.boyko.mike.groceries;

import android.content.ClipData;
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

import com.example.boyko.mike.groceries.db.models.Category;
import com.example.boyko.mike.groceries.db.models.ListItemComplete;

import java.util.ArrayList;
import java.util.List;


public class ItemArrayAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_CATEGORY = 1;

    // Keep these internal lists because they are how the list gets built
    private List<Category> categories;
    private List<ListItemComplete> items;

    // These are the actual rows in the ListView
    private ArrayList<Object> mData;

    private Category uncategorized;

    private LayoutInflater mInflater;


    public ItemArrayAdapter(Context context, int textViewResourceId) {
        categories = new ArrayList<Category>();
        items = new ArrayList<ListItemComplete>();
        mData = new ArrayList<Object>();

        uncategorized = new Category(Category.UNCATEGORIZED);

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void setCategories(List<Category> categories) {
        this.categories = categories;
        recalcList();
    }


    public void setItems(List<ListItemComplete> items) {
        this.items = items;
        recalcList();
    }


    public void recalcList() {

        // First, wipe out the list.
        mData.clear();

        // Then add the uncategorized type
        addSectionHeaderItem(uncategorized);

        // Then add all the categories
        for (Category category: categories) {
            addSectionHeaderItem(category);
        }

        // And then add all the items
        for (ListItemComplete item: items) {
            addItem(item);
        }

        // Finally, notify the view itself that the data has changed.
        notifyDataSetChanged();
    }


    public void addItem(ListItemComplete listItem) {

        Category category = listItem.category;

        if (category == null) {
            category = uncategorized;
        }

        boolean added = false;
        for (int i=0; i<mData.size(); i++) {
            Object obj = mData.get(i);

            if (obj instanceof Category) {
                if (((Category)obj).id == category.id) {
                    mData.add(i+1, listItem);
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
    }


    public void addSectionHeaderItem(Category item) {
        mData.add(item);
    }


    /**
     * When updating an listItem it is best to re-add it again if
     * the category changed.
     * @param position
     * @param listItem
     */
    public void updateItem(int position, ListItemComplete listItem) {

        Object obj = mData.get(position);

        if (obj == null) {
            // For some reason there was no object at that position, so just add it.
            addItem(listItem);
            return;
        }

        if (obj instanceof Category) {
            // For some reason that position is occupied by a Category.
            // Just add the listItem.
            addItem(listItem);
            return;
        }

        if (! categoriesEqual(listItem.category, ((ListItemComplete)obj).category)) {
            // The category changed. Remove the listItem from the list and re-add it.
            mData.remove(position);
            addItem(listItem);
            return;
        }

        // The listItem changed, but the category did not. Just replace it in-situ.
        mData.set(position, listItem);
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
     * @param position The position in the ListItem ArrayList.
     * @return null if not found or the object is a CategoryHeader. An ListItem otherwise.
     */
    public ListItemComplete getListItem(int position) {
        if (getItemViewType(position) == TYPE_CATEGORY) {
            return null;
        }
        return (ListItemComplete)getItem(position);
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
                    holder.item = (TextView) convertView.findViewById(R.id.listItem);
                    holder.checked = (CheckBox) convertView.findViewById(R.id.checkBox1);

                    holder.checked.setOnClickListener( new View.OnClickListener() {
                        public void onClick(View v) {
                            CheckBox cb = (CheckBox) v ;
                            ListItemComplete listItem = (ListItemComplete) cb.getTag();
                            Log.v("ConvertView","Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked());
                            listItem.listItem.checked = cb.isChecked();

                            ViewParent parent = v.getParent();

                            if (parent != null) {
                                ViewGroup parentGroup = (ViewGroup)parent;
                                TextView tv = (TextView) parentGroup.findViewById(R.id.listItem);

                                if (listItem.listItem.checked) {
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
                ListItemComplete listItem = (ListItemComplete) mData.get(position);
                holder.item.setText(listItem.toString());
                holder.checked.setChecked(listItem.listItem.checked);
                holder.checked.setTag(listItem);
                break;
        }

        return convertView;
    }

}

