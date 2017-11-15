package com.example.boyko.mike.groceries;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.boyko.mike.groceries.db.AppDatabase;
import com.example.boyko.mike.groceries.db.models.ListItem;

import java.util.List;

/**
 * This is a ViewModel class for the Main Activity.
 * It's purpose is to sync the list items in the View
 * when the Model changes.
 */

public class ListItemViewModel extends AndroidViewModel {

    private final LiveData<List<ListItem>> items;

    private AppDatabase appDatabase;


    public ListItemViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(this.getApplication());

        items = appDatabase.listItemDao().getAll();

    }


    public LiveData<List<ListItem>> getListItems() {
        return items;
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
}
