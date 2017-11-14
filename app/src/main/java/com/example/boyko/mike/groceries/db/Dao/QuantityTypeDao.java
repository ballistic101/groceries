package com.example.boyko.mike.groceries.db.Dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.boyko.mike.groceries.db.models.QuantityType;

import java.util.List;

/**
 * The Data Access Object for Quantity Types
 */

public interface QuantityTypeDao {


    @Query("SELECT COUNT(*) FROM quantity_type")
    int count();

    @Query("SELECT * FROM quantity_type ")
    List<QuantityType> getAll();

    @Query("SELECT * FROM quantity_type WHERE single = :single")
    QuantityType findBySingle(String single);

    @Insert
    void insertAll(List<QuantityType> quantityTypes);

    @Insert
    long insert(QuantityType quantityType);

    @Update
    void update(QuantityType quantityType);

    @Delete
    void delete(QuantityType quantityType);
}
