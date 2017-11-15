package com.example.boyko.mike.groceries;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.boyko.mike.groceries.db.AppDatabase;
import com.example.boyko.mike.groceries.db.models.Category;

import java.util.List;

/**
 * This is a ViewModel class for the Main Activity.
 * It's purpose is to sync the list items in the View
 * when the Model changes.
 */

public class CategoryViewModel extends AndroidViewModel {

    private final LiveData<List<Category>> categories;

    private AppDatabase appDatabase;


    public CategoryViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(this.getApplication());

        categories = appDatabase.categoryDao().getAll();

    }


    public LiveData<List<Category>> getCategories() {
        return categories;
    }


    public void deleteCategory(Category category) {
        new deleteAsyncTask(appDatabase).execute(category);
    }

    private static class deleteAsyncTask extends AsyncTask<Category, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Category... params) {
            db.categoryDao().delete(params[0]);
            return null;
        }
    }


    public void addCategory(final Category category) {
        new addAsyncTask(appDatabase).execute(category);
    }


    private static class addAsyncTask extends AsyncTask<Category, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Category... params) {
            db.categoryDao().insert(params[0]);
            return null;
        }
    }
}
