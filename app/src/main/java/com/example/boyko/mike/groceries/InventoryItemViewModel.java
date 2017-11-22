package com.example.boyko.mike.groceries;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.boyko.mike.groceries.db.AppDatabase;
import com.example.boyko.mike.groceries.db.models.InventoryItem;
import com.example.boyko.mike.groceries.db.models.ListItem;

import java.util.List;

/**
 * This is a ViewModel class for the Main Activity.
 * It's purpose is to sync the list items in the View
 * when the Model changes.
 */

public class InventoryItemViewModel extends AndroidViewModel {

    private final LiveData<List<InventoryItem>> items;

    private AppDatabase appDatabase;


    public InventoryItemViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(this.getApplication());

        items = appDatabase.inventoryItemDao().getAll();

    }


    public LiveData<List<InventoryItem>> getInventoryItems() {
        return items;
    }


    public void deleteInventoryItem(InventoryItem item) {
        new deleteAsyncTask(appDatabase).execute(item);
    }

    private static class deleteAsyncTask extends AsyncTask<InventoryItem, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final InventoryItem... params) {
            db.inventoryItemDao().delete(params[0]);
            return null;
        }
    }


    public void addInventoryItem(final InventoryItem item) {
        new addAsyncTask(appDatabase).execute(item);
    }


    private static class addAsyncTask extends AsyncTask<InventoryItem, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final InventoryItem... params) {
            db.inventoryItemDao().insert(params[0]);
            return null;
        }
    }
}
