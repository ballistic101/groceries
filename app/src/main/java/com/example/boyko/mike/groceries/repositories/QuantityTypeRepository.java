package com.example.boyko.mike.groceries.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

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

    private AppDatabase appDatabase;
    private LiveData<List<QuantityType>> quantityTypes;

    public QuantityTypeRepository(@NonNull Application application) {
        appDatabase = AppDatabase.getInstance(application);

        quantityTypes = appDatabase.quantityTypeDao().getAll();
    }

    public LiveData<List<QuantityType>> getTypes() {
        return quantityTypes;
    }

}
