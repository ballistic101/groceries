package com.example.boyko.mike.groceries.repositories;

import com.example.boyko.mike.groceries.Groceries;
import com.example.boyko.mike.groceries.db.AppDatabase;
import com.example.boyko.mike.groceries.db.models.ListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates how List Items are gathered.
 */

public class ListItemRepository {

    public ArrayList<ListItem> getListItems() {
        List<ListItem> items = AppDatabase.getInstance(Groceries.getAppContext()).listItemDao().getAll();
        return new ArrayList<ListItem>(items);
    }
}
