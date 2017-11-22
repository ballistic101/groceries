package com.example.boyko.mike.groceries.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.boyko.mike.groceries.db.AppDatabase;
import com.example.boyko.mike.groceries.db.models.ListItem;

import java.util.List;

/**
 * This class encapsulates how the ListItems
 * are used.
 * This allows the Repository to be mocked to return
 * faked ListItem objects.
 */

public class ListItemRepository {

    private AppDatabase appDatabase;

    public ListItemRepository(@NonNull Application application) {
        appDatabase = AppDatabase.getInstance(application);
    }

    public LiveData<List<ListItem>> getItems() {
        //new getAsyncTask(appDatabase, items).execute();
        return appDatabase.listItemDao().getAll();
    }


    public void deleteItem(ListItem item) {
        new deleteAsyncTask(appDatabase).execute(item);
    }

    private static class deleteAsyncTask extends AsyncTask<ListItem, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final ListItem... params) {
            db.listItemDao().delete(params[0]);
            return null;
        }
    }


    public void addItem(final ListItem item) {
        new addAsyncTask(appDatabase).execute(item);
    }


    private static class addAsyncTask extends AsyncTask<ListItem, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final ListItem... params) {
            db.listItemDao().insert(params[0]);
            return null;
        }
    }


    public void saveItem(final ListItem item) {
        new saveAsyncTask(appDatabase).execute(item);
    }


    private static class saveAsyncTask extends AsyncTask<ListItem, Void, Void> {

        private AppDatabase db;

        saveAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final ListItem... params) {
            Log.i("Groceries", "Updating List Item (" + params[0].name + ")");
            db.listItemDao().update(params[0]);
            return null;
        }
    }

}