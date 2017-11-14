package com.example.boyko.mike.groceries.db.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.boyko.mike.groceries.db.models.Category;

import java.util.List;

/**
 * This is the interface for Room for a Category.
 */

@Dao
public interface CategoryDao {

    @Query("SELECT COUNT(*) FROM category")
    int count();

    @Query("SELECT * FROM category ")
    List<Category> getAll();

    @Query("SELECT * FROM category WHERE name = :name")
    Category findByName(String name);

    @Insert
    void insertAll(List<Category> categories);

    @Insert
    long insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);
}
