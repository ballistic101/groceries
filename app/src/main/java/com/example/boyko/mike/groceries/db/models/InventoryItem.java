package com.example.boyko.mike.groceries.db.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 *
 * This is an Inventory Item.
 * All Items that have been typed into the system
 * get added here. That allows for auto-complete
 * when adding an Item to your list.
 */

@Entity(tableName = "inventory_item",
        foreignKeys = {
        @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.SET_NULL
        )
})
public class InventoryItem {

    public static final String TAG = "InventoryItem";

    @PrimaryKey(autoGenerate = true)
    public Long id;                // The internal database id

    @ColumnInfo(name = "name")
    public String name;           // The name of the ListItem

    @ColumnInfo(name="category_id")
    public Long categoryId;


    @Ignore
    public InventoryItem(String name) {
        this.name = name;
        this.categoryId = null;
    }


    public InventoryItem(String name, Long categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }

}
