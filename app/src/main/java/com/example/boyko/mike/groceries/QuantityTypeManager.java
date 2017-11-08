package com.example.boyko.mike.groceries;

import java.util.ArrayList;

/**
 * This class encapsulates how the Quantity Types
 * are gathered.
 * This allows the Manager to be mocked to return
 * faked QuantityType objects.
 */

public class QuantityTypeManager {

    private ArrayList<QuantityType> types;

    public QuantityTypeManager() {

        types = new ArrayList<QuantityType>();

        // @todo - This should be gathering the types from a database.
        generateTypes();
    }


    public ArrayList<QuantityType> getTypes() {
        return types;
    }


    /**
     * A testing class to manually generate a bunch of QuantityType objects.
     */
    private void generateTypes() {

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
}
