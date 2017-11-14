package com.example.boyko.mike.groceries.db.Dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.boyko.mike.groceries.db.models.ListItem;

import java.util.List;

/**
 * The Data Access Object for List Items.
 * These are items which are actually in the ListView.
 */

public interface ListItemDao {

    @Query("SELECT COUNT(*) FROM list_item")
    int count();

    @Query("SELECT * FROM list_item ")
    List<ListItem> getAll();

    @Query("SELECT * FROM list_item WHERE name = :name")
    ListItem findByName(String name);

    @Insert
    void insertAll(List<ListItem> listItems);

    @Insert
    long insert(ListItem listItem);

    @Update
    void update(ListItem listItem);

    @Delete
    void delete(ListItem listItem);

}
