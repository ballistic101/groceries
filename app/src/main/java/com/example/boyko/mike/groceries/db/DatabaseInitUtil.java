package com.example.boyko.mike.groceries.db;

/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.example.boyko.mike.groceries.db.models.QuantityType;
import com.example.boyko.mike.groceries.repositories.CategoryRepository;
import com.example.boyko.mike.groceries.db.models.Category;

import java.util.ArrayList;
import java.util.List;


/** Generates dummy data and inserts them into the database */
class DatabaseInitUtil {

    static void initializeDb(AppDatabase db) {
        List<Category> categories = new ArrayList<Category>();
        generateCategoryData(categories);

        List<QuantityType> quantityTypes = new ArrayList<QuantityType>();
        generateQuantityTypeData(quantityTypes);

        insertData(db, categories, quantityTypes);
    }

    private static void generateCategoryData(List<Category> categories) {
        categories.add(new Category("Cub", "#ae9c9c"));
        categories.add(new Category("Costco", "#90a1a3"));
        categories.add(new Category("Kwik Trip", "#97a585"));
    }

    private static void generateQuantityTypeData(List<QuantityType> types) {

        types.add(new QuantityType(1, "none", "none"));
        types.add(new QuantityType(2, "piece", "pieces"));
        types.add(new QuantityType(3, "bag", "bags"));
        types.add(new QuantityType(4, "bottle", "bottles"));
        types.add(new QuantityType(5, "box", "boxes"));
        types.add(new QuantityType(6, "case", "cases"));
        types.add(new QuantityType(7, "jar", "jars"));
        types.add(new QuantityType(8, "can", "cans"));
        types.add(new QuantityType(9, "bunch", "bunches"));
        types.add(new QuantityType(10, "dozen", "dozen"));
        types.add(new QuantityType(11, "lbs", "lbs"));
        types.add(new QuantityType(12, "qt", "qt"));
        types.add(new QuantityType(13, "oz", "oz"));
        types.add(new QuantityType(14, "cup", "cups"));
        types.add(new QuantityType(15, "gallon", "gallons"));
        types.add(new QuantityType(16, "tbsp", "tbsp"));
        types.add(new QuantityType(17, "tsp", "tsp"));
        types.add(new QuantityType(18, "g", "g"));
        types.add(new QuantityType(19, "kg", "kg"));
        types.add(new QuantityType(20, "l", "l"));
        types.add(new QuantityType(21, "ml", "ml"));
        types.add(new QuantityType(22, "pt", "pt"));
    }


    private static void insertData(AppDatabase db, List<Category> categories, List<QuantityType> quantityTypes) {
        db.beginTransaction();
        try {
            db.categoryDao().insertAll(categories);
            db.quantityTypeDao().insertAll(quantityTypes);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}