package com.example.boyko.mike.groceries;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.boyko.mike.groceries.db.AppDatabase;
import com.example.boyko.mike.groceries.db.models.ListItem;
import com.example.boyko.mike.groceries.repositories.ListItemRepository;

import java.util.List;

/**
 * This is a ViewModel class for the Main Activity.
 * It's purpose is to sync the list items in the View
 * when the Model changes.
 */

public class ListItemViewModel extends AndroidViewModel {

    private ListItemRepository repo;
    private LiveData<List<ListItem>> items;

    public ListItemViewModel(@NonNull Application application) {
        super(application);

        repo = new ListItemRepository(application);

        // Note that the ListItem is being assigned the MutableLiveData.
        // This works because that class extends LiveData.
        items = new MutableLiveData<List<ListItem>>();
        items = repo.getItems();
    }


    public LiveData<List<ListItem>> getListItems() {
        return items;
    }


    public void deleteItem(ListItem item) {
        repo.deleteItem(item);
    }


    public void addItem(final ListItem item) {
        repo.addItem(item);
    }


    public void saveItem(final ListItem item) {
        repo.saveItem(item);
    }
}
