package com.example.boyko.mike.groceries.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.boyko.mike.groceries.db.models.ListItem;
import com.example.boyko.mike.groceries.db.models.ListItemComplete;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

import java.util.List;

/**
 * The Data Access Object for List Items.
 * These are items which are actually in the ListView.
 */

@Dao
public interface ListItemDao {

    @Query("SELECT COUNT(*) FROM list_item")
    int count();

    @Query("SELECT * FROM list_item")
    LiveData<List<ListItem>> getAll();

    @Query("SELECT li.id as li_id, li.name as li_name, li.quantity as li_quantity, li.quantity_type_id as li_quantity_type_id, li.category_id as li_category_id, li.coupon as li_coupon, li.notes as li_notes, li.checked as li_checked," +
            " qt.id as qt_id, qt.single as qt_single, qt.plural as qt_plural," +
            " c.id as cat_id, c.name as cat_name, c.color as cat_color" +
            " FROM list_item AS li" +
            " LEFT JOIN quantity_type AS qt ON (qt.id = li.quantity_type_id)" +
            " LEFT JOIN category AS c ON (c.id = li.category_id)")
    LiveData<List<ListItemComplete>> getAllComplete();

    @Query("SELECT * FROM list_item WHERE name = :name")
    ListItem findByName(String name);

    @Insert(onConflict = REPLACE)
    void insertAll(List<ListItem> listItems);

    @Insert(onConflict = REPLACE)
    long insert(ListItem listItem);

    @Update
    void update(ListItem listItem);

    @Delete
    void delete(ListItem listItem);

}
