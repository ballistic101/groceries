package com.example.boyko.mike.groceries.EditItem;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.boyko.mike.groceries.db.models.Category;

import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends ArrayAdapter {

    // This is the list of Categories
    private List<Category> categories;

    private Category uncategorized;

    int id;


    public CategoryAdapter(Context context, int spinnerResourceId) {
        super(context, spinnerResourceId);

        uncategorized = new Category(Category.UNCATEGORIZED);

        categories = new ArrayList<Category>();
        categories.add(uncategorized);

        // Dump an "empty" array in to start
        addAll(categories);

    }


    public void setCategories(List<Category> categories) {
        this.categories = categories;

        // Stick "uncategorized" at the front of the list
        this.categories.add(0, uncategorized);

        clear();
        addAll(categories);
        notifyDataSetChanged();
    }

}

