package com.example.boyko.mike.groceries.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import com.example.boyko.mike.groceries.db.Dao.InventoryItemDao;
import com.example.boyko.mike.groceries.db.Dao.ListItemDao;
import com.example.boyko.mike.groceries.db.Dao.QuantityTypeDao;
import com.example.boyko.mike.groceries.db.models.Category;
import com.example.boyko.mike.groceries.db.Dao.CategoryDao;
import com.example.boyko.mike.groceries.db.models.InventoryItem;
import com.example.boyko.mike.groceries.db.models.ListItem;
import com.example.boyko.mike.groceries.db.models.QuantityType;

/**
 * This is the Room interface for database.
 * It should define all the Dao objects for the models.
 */

@Database(
        entities = {
                Category.class,
                QuantityType.class,
                ListItem.class,
                InventoryItem.class
        },
        version = 3
)
public abstract class AppDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "groceries";

    public abstract CategoryDao categoryDao();

    public abstract QuantityTypeDao quantityTypeDao();

    public abstract ListItemDao listItemDao();

    public abstract InventoryItemDao inventoryItemDao();

    private static AppDatabase sInstance;

    /**
     * Gets the singleton instance of AppDatabase.
     *
     * @param context The context.
     * @return The singleton instance of AppDatabase.
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
            sInstance.populateInitialData();
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }


    /**
     * Inserts the data into the database if it is currently empty.
     */
    private void populateInitialData() {
            new AsyncTask<AppDatabase, Void, Void>() {

                @Override
                protected Void doInBackground(AppDatabase... dbs) {
                    if (categoryDao().count() == 0) {
                        DatabaseInitUtil.initializeDb(dbs[0]);
                    }
                    return null;
                }
            }.execute(this);
    }
}