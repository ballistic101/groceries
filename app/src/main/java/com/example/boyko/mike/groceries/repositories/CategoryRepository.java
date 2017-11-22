package com.example.boyko.mike.groceries.repositories;

import android.util.Log;

import com.example.boyko.mike.groceries.Groceries;
import com.example.boyko.mike.groceries.db.AppDatabase;
import com.example.boyko.mike.groceries.db.models.Category;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * This class encapsulates how the Categories
 * are gathered.
 * This allows the Repository to be mocked to return
 * faked Category objects.
 */

public class CategoryRepository {

    public static final String UNCATEGORIZED = "Uncategorized";

    private static CategoryRepository repo;

    private ArrayList<Category> categories;

    // A latch for loading the initial categories
    private final CountDownLatch latch = new CountDownLatch(1);

    private Category uncategorized;

    /**
     * A static getter method which turns this class
     * into a singleton.
     *
     * @return An instance of CategoryRepository.
     */
    public static CategoryRepository getInstance() {
        if (repo == null) {
            repo = new CategoryRepository();
        }
        return repo;
    }

    private CategoryRepository() {

        categories = new ArrayList<Category>();
        generateCategories();
    }


    public ArrayList<Category> getCategories() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return categories;
    }


    public void addCategory(Category category) {
        categories.add(category);
    }

    public void addCategory(String name) {
        Category category = new Category(name);
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

        if (uncategorized == null) {
            for (int i=0; i<categories.size(); i++) {
                Category cat = categories.get(i);
                if (cat.name.equals(UNCATEGORIZED)) {
                    uncategorized = cat;
                    break;
                }
            }
        }
        return uncategorized;
    }


    /**
     * A testing class to manually generate a bunch of QuantityType objects.
     */
    private void generateCategories() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                //categories.addAll(AppDatabase.getInstance(Groceries.getAppContext()).categoryDao().getAll());
                Log.i("Groceries", "Loaded all the categories. Count = " + categories.size());
                latch.countDown();
            }
        };

        thread.start();
    }
}
