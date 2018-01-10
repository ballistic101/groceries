package com.example.boyko.mike.groceries.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.boyko.mike.groceries.db.AppDatabase;
import com.example.boyko.mike.groceries.db.models.Category;

import java.util.List;

/**
 * This class encapsulates how the Categories
 * are gathered.
 * This allows the Repository to be mocked to return
 * faked Category objects.
 */

public class CategoryRepository {

    private AppDatabase appDatabase;
    private LiveData<List<Category>> categories;

    public CategoryRepository(@NonNull Application application) {
        appDatabase = AppDatabase.getInstance(application);

        categories = appDatabase.categoryDao().getAll();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }
}
