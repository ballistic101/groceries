package com.example.boyko.mike.groceries.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.boyko.mike.groceries.db.models.ListItem;
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

    @Query("SELECT * FROM list_item ")
    LiveData<List<ListItem>> getAll();

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
