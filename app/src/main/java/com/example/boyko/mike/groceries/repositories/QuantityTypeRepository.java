package com.example.boyko.mike.groceries.repositories;

import com.example.boyko.mike.groceries.Groceries;
import com.example.boyko.mike.groceries.db.AppDatabase;
import com.example.boyko.mike.groceries.db.models.QuantityType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * This class encapsulates how the Quantity Types
 * are gathered.
 * This allows the Manager to be mocked to return
 * faked QuantityType objects.
 */

public class QuantityTypeRepository {

    public ArrayList<QuantityType> getTypes() {
        List<QuantityType> types = AppDatabase.getInstance(Groceries.getAppContext()).quantityTypeDao().getAll();
        return new ArrayList<QuantityType>(types);
    }

}
