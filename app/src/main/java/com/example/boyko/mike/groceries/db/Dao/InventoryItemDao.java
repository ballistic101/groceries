package com.example.boyko.mike.groceries.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.boyko.mike.groceries.db.models.InventoryItem;

import java.util.List;

/**
 * The Data Access Object for List Items.
 * These are items which are actually in the ListView.
 */

@Dao
public interface InventoryItemDao {

    @Query("SELECT COUNT(*) FROM inventory_item")
    int count();

    @Query("SELECT * FROM inventory_item ")
    LiveData<List<InventoryItem>> getAll();

    @Query("SELECT * FROM inventory_item WHERE name = :name")
    InventoryItem findByName(String name);

    @Insert
    void insertAll(List<InventoryItem> listItems);

    @Insert
    long insert(InventoryItem listItem);

    @Update
    void update(InventoryItem listItem);

    @Delete
    void delete(InventoryItem listItem);

}
