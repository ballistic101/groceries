package com.example.boyko.mike.groceries;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.boyko.mike.groceries.db.AppDatabase;
import com.example.boyko.mike.groceries.db.models.QuantityType;

import java.util.List;

/**
 * This is a ViewModel class for the Main Activity.
 * It's purpose is to sync the list items in the View
 * when the Model changes.
 */

public class QuantityTypeViewModel extends AndroidViewModel {

    private final LiveData<List<QuantityType>> types;

    private AppDatabase appDatabase;


    public QuantityTypeViewModel(@NonNull Application application) {
        super(application);

        appDatabase = AppDatabase.getInstance(this.getApplication());

        types = appDatabase.quantityTypeDao().getAll();

    }


    public LiveData<List<QuantityType>> getTypes() {
        return types;
    }


    public void deleteType(QuantityType type) {
        new deleteAsyncTask(appDatabase).execute(type);
    }

    private static class deleteAsyncTask extends AsyncTask<QuantityType, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final QuantityType... params) {
            db.quantityTypeDao().delete(params[0]);
            return null;
        }
    }


    public void addType(final QuantityType type) {
        new addAsyncTask(appDatabase).execute(type);
    }


    private static class addAsyncTask extends AsyncTask<QuantityType, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final QuantityType... params) {
            db.quantityTypeDao().insert(params[0]);
            return null;
        }
    }
}
