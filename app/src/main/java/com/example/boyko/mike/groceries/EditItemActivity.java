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

import com.example.boyko.mike.groceries.db.models.Category;
import com.example.boyko.mike.groceries.db.models.ListItem;
import com.example.boyko.mike.groceries.db.models.QuantityType;
import com.example.boyko.mike.groceries.repositories.CategoryRepository;
import com.example.boyko.mike.groceries.repositories.QuantityTypeRepository;

import java.util.ArrayList;
import java.util.Locale;

public class EditItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public final static String ACTION_TAG = "action";

    public final static String ACTION_UPDATE = "update";
    public final static String ACTION_DELETE = "delete";
    public final static String ACTION_CANCEL = "cancel";

    // The ListItem being edited
    ListItem listItem;

    // The main activity passes in information about the listItem for it's use
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
        QuantityTypeRepository qtyMgr = new QuantityTypeRepository();
        ArrayList<QuantityType> quantityTypes = qtyMgr.getTypes();
        ArrayAdapter<QuantityType> dataAdapter = new ArrayAdapter<QuantityType>(this, android.R.layout.simple_spinner_item, quantityTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantityType.setAdapter(dataAdapter);
        // Spinner click listener
        quantityType.setOnItemSelectedListener(this);

        category = (Spinner) findViewById(R.id.category);
        CategoryRepository catMgr = CategoryRepository.getInstance();
        ArrayList<Category> categories = catMgr.getCategories();
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryArrayAdapter);
        // Spinner click listener
        category.setOnItemSelectedListener(this);


        coupon = (CheckBox) findViewById(R.id.coupon);
        notes = (EditText) findViewById(R.id.notes);

        // If there is an intent, then fill in the listItem
        Intent intent = getIntent();
        if (intent != null) {
            listItem = intent.getParcelableExtra(ListItem.TAG);
            position = intent.getIntExtra(MainActivity.POSITION_TAG, MainActivity.ILLEGAL_POSITION);

            name.setText(listItem.name);
            quantity.setText(String.format(Locale.US, "%d", listItem.quantity));
            if (listItem.quantityType != null) {

                // Loop through the quantity types because dataAdapter.getPosition is calling
                // toString() under the covers for it's comparison and never finds the object.
                for (int i=0; i<dataAdapter.getCount(); i++) {
                    if (dataAdapter.getItem(i).id == listItem.quantityType.id) {
                        quantityType.setSelection(i);
                    }
                }
            }

            if (listItem.category != null) {
                // Loop through the categories because categoryArrayAdapter.getPosition is calling
                // toString() under the covers for it's comparison and never finds the object.
                for (int i=0; i<categoryArrayAdapter.getCount(); i++) {
                    if (categoryArrayAdapter.getItem(i).id == listItem.category.id) {
                        category.setSelection(i);
                    }
                }
            }

            coupon.setChecked(listItem.coupon);
            notes.setText(listItem.notes);
        }
        else {
            listItem = new ListItem("Unknown");
        }

    }


    public void increaseQuantity(View view) {
        listItem.quantity++;
        quantity.setText(String.format(Locale.US,"%d", listItem.quantity));
    }


    public void decreaseQuantity(View view) {

        // Ensure that the quantity cannot become nothing or empty.
        if (listItem.quantity <= 1) {
            listItem.quantity = 1;
        }
        else {
            listItem.quantity--;
        }
        quantity.setText(String.format(Locale.US,"%d", listItem.quantity));
    }

    /**
     * Save the current ListItem and leave the activity.
     *
     * @param v
     */
    public void saveItem(View v) {

        // Update the ListItem object
        listItem.name = name.getText().toString();
        listItem.quantity = Integer.parseInt(quantity.getText().toString());
        //listItem.quantityType = quantityType.;
        //listItem.category = category;
        listItem.coupon = coupon.isChecked();
        listItem.notes = notes.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(ListItem.TAG, listItem);
        intent.putExtra(MainActivity.POSITION_TAG, position);
        intent.putExtra(EditItemActivity.ACTION_TAG, EditItemActivity.ACTION_UPDATE);
        setResult(IntentConstants.EDIT_ITEM, intent);
        finish();
    }


    /**
     * Delete the current ListItem and leave the activity.
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
     * Stop editing and do not make any changes to the listItem.
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
                    listItem.quantityType = null;
                } else {
                    listItem.quantityType = quantity_type;
                }
                break;
            case R.id.category:
                Category category = (Category) parent.getItemAtPosition(position);
                listItem.category = category;
                break;
        }
    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
