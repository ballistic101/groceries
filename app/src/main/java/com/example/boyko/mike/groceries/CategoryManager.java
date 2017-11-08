package com.example.boyko.mike.groceries;

import android.util.Log;

import java.util.ArrayList;

/**
 * This class encapsulates how the Categories
 * are gathered.
 * This allows the Manager to be mocked to return
 * faked Category objects.
 */

public class CategoryManager {

    private ArrayList<Category> categories;

    public CategoryManager() {

        categories = new ArrayList<Category>();

        // @todo - This should be gathering the types from a database.
        generateCategories();
    }


    public ArrayList<Category> getCategories() {
        return categories;
    }


    public void addCategory(Category category) {
        categories.add(category);
    }

    public void deleteCategory(Category category) {
        for (int i=0; i<categories.size(); i++) {
            if (categories.get(i).id == category.id) {
                categories.remove(i);
                return;
            }
        }
        Log.e("Groceries", "Attempt to delete Category with id " + category.id + " that does not exist!");
    }

    /**
     * A testing class to manually generate a bunch of QuantityType objects.
     */
    private void generateCategories() {

        addCategory(new Category(1, "Uncategorized"));
        addCategory(new Category(2, "Cub"));
        addCategory(new Category(1, "Costco"));
        addCategory(new Category(1, "Kwik Trip"));
    }

}
