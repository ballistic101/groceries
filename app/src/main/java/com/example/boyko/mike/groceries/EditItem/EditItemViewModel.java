package com.example.boyko.mike.groceries.EditItem;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.boyko.mike.groceries.db.models.Category;
import com.example.boyko.mike.groceries.db.models.QuantityType;
import com.example.boyko.mike.groceries.repositories.CategoryRepository;
import com.example.boyko.mike.groceries.repositories.QuantityTypeRepository;

import java.util.List;


/**
 * This is a ViewModel class for the Main Activity.
 * It's purpose is to sync the list items in the View
 * when the Model changes.
 */

public class EditItemViewModel extends AndroidViewModel {

    private QuantityTypeRepository quantityTypeRepo;
    private LiveData<List<QuantityType>> types;

    private CategoryRepository categoryRepo;
    private LiveData<List<Category>> categories;

    public EditItemViewModel(@NonNull Application application) {
        super(application);

        quantityTypeRepo = new QuantityTypeRepository(application);

        // Note that the ListItem is being assigned the MutableLiveData.
        // This works because that class extends LiveData.
        types = new MutableLiveData<List<QuantityType>>();
        types = quantityTypeRepo.getTypes();

        categoryRepo = new CategoryRepository(application);

        categories = new MutableLiveData<>();
        categories = categoryRepo.getCategories();
    }


    public LiveData<List<QuantityType>> getQuantityTypes() {
        return types;
    }

    public LiveData<List<Category>> getCategories() { return categories; }

 }
