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

    public static final String UNCATEGORIZED = "Uncategorized";

    private static CategoryManager mgr;

    private ArrayList<Category> categories;

    /**
     * A static getter method which turns this class
     * into a singleton.
     *
     * @return An instance of CategoryManager.
     */
    public static CategoryManager getInstance() {
        if (mgr == null) {
            mgr = new CategoryManager();
        }
        return mgr;
    }

    private CategoryManager() {

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

    public void addCategory(String name) {
        int id = getNewId();
        Category category = new Category(id, name);
        addCategory(category);
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


    public Category getUncategorized( ) {
        for (int i=0; i<categories.size(); i++) {
            Category cat = categories.get(i);
            if (cat.name == UNCATEGORIZED) {
                return cat;
            }
        }
        return null;
    }


    /**
     * A testing class to manually generate a bunch of QuantityType objects.
     */
    private void generateCategories() {

        addCategory(new Category(getNewId(), UNCATEGORIZED));
        addCategory(new Category(getNewId(), "Cub", "#ae9c9c"));
        addCategory(new Category(getNewId(), "Costco", "#90a1a3"));
        addCategory(new Category(getNewId(), "Kwik Trip", "#97a585"));
    }


    /**
     * Without a database, just go through the list of Categories and add
     * 1 to the highest one.
     * @return int  The next id to use.
     */
    private int getNewId() {

        int max = 0;
        for (Category category: categories) {
            if (category.id > max) {
                max = category.id;
            }
        }

        return max + 1;
    }
}
