package com.example.boyko.mike.groceries;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class EditItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public final static String ACTION_TAG = "action";

    public final static String ACTION_UPDATE = "update";
    public final static String ACTION_DELETE = "delete";
    public final static String ACTION_CANCEL = "cancel";

    // The Item being edited
    Item item;

    // The main activity passes in information about the item for it's use
    int position;

    EditText name;
    TextView quantity;
    Spinner quantityType;
    Spinner category;
    CheckBox coupon;
    EditText notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);


        name = (EditText) findViewById(R.id.itemName);
        quantity = (TextView) findViewById(R.id.quantity);

        quantityType = (Spinner) findViewById(R.id.quantityType);
        QuantityTypeManager qtyMgr = new QuantityTypeManager();
        ArrayList<QuantityType> quantityTypes = qtyMgr.getTypes();
        ArrayAdapter<QuantityType> dataAdapter = new ArrayAdapter<QuantityType>(this, android.R.layout.simple_spinner_item, quantityTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantityType.setAdapter(dataAdapter);
        // Spinner click listener
        quantityType.setOnItemSelectedListener(this);

        category = (Spinner) findViewById(R.id.category);
        CategoryManager catMgr = new CategoryManager();
        ArrayList<Category> categories = catMgr.getCategories();
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryArrayAdapter);
        // Spinner click listener
        category.setOnItemSelectedListener(this);


        coupon = (CheckBox) findViewById(R.id.coupon);
        notes = (EditText) findViewById(R.id.notes);

        // If there is an intent, then fill in the item
        Intent intent = getIntent();
        if (intent != null) {
            item = intent.getParcelableExtra(Item.TAG);
            position = intent.getIntExtra(MainActivity.POSITION_TAG, MainActivity.ILLEGAL_POSITION);

            name.setText(item.name);
            quantity.setText(String.format(Locale.US, "%d", item.quantity));
            if (item.quantityType != null) {

                // Loop through the quantity types because dataAdapter.getPosition is calling
                // toString() under the covers for it's comparison and never finds the object.
                for (int i=0; i<dataAdapter.getCount(); i++) {
                    if (dataAdapter.getItem(i).id == item.quantityType.id) {
                        quantityType.setSelection(i);
                    }
                }
            }

            if (item.category != null) {
                // Loop through the categories because categoryArrayAdapter.getPosition is calling
                // toString() under the covers for it's comparison and never finds the object.
                for (int i=0; i<categoryArrayAdapter.getCount(); i++) {
                    if (categoryArrayAdapter.getItem(i).id == item.category.id) {
                        category.setSelection(i);
                    }
                }
            }

            coupon.setChecked(item.coupon);
            notes.setText(item.notes);
        }
        else {
            item = new Item("Unknown");
        }

    }


    public void increaseQuantity(View view) {
        item.quantity++;
        quantity.setText(String.format(Locale.US,"%d", item.quantity));
    }


    public void decreaseQuantity(View view) {

        // Ensure that the quantity cannot become nothing or empty.
        if (item.quantity <= 1) {
            item.quantity = 1;
        }
        else {
            item.quantity--;
        }
        quantity.setText(String.format(Locale.US,"%d", item.quantity));
    }

    /**
     * Save the current Item and leave the activity.
     *
     * @param v
     */
    public void saveItem(View v) {

        // Update the Item object
        item.name = name.getText().toString();
        item.quantity = Integer.parseInt(quantity.getText().toString());
        //item.quantityType = quantityType.;
        //item.category = category;
        item.coupon = coupon.isChecked();
        item.notes = notes.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(Item.TAG, item);
        intent.putExtra(MainActivity.POSITION_TAG, position);
        intent.putExtra(EditItemActivity.ACTION_TAG, EditItemActivity.ACTION_UPDATE);
        setResult(IntentConstants.EDIT_ITEM, intent);
        finish();
    }


    /**
     * Delete the current Item and leave the activity.
     *
     * @param v
     */
    public void deleteItem(View v) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.POSITION_TAG, position);
        intent.putExtra(EditItemActivity.ACTION_TAG, EditItemActivity.ACTION_DELETE);
        setResult(IntentConstants.EDIT_ITEM, intent);
        finish();
    }

    /**
     * Stop editing and do not make any changes to the item.
     *
     * @param v
     */
    public void cancelItem(View v) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.POSITION_TAG, position);
        intent.putExtra(EditItemActivity.ACTION_TAG, EditItemActivity.ACTION_CANCEL);
        setResult(IntentConstants.EDIT_ITEM, intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch(parent.getId()) {
            case R.id.quantityType:
                QuantityType quantity_type = (QuantityType) parent.getItemAtPosition(position);

                if (quantity_type.single.equals("none") || quantity_type == null) {
                    item.quantityType = null;
                } else {
                    item.quantityType = quantity_type;
                }
                break;
            case R.id.category:
                Category category = (Category) parent.getItemAtPosition(position);
                item.category = category;
                break;
        }
    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
