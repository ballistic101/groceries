package com.example.boyko.mike.groceries;

import android.app.Application;
import android.content.Context;

/**
 * Created by mike on 11/9/17.
 */

public class Groceries extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        Groceries.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Groceries.context;
    }

}
