package com.example.boyko.mike.groceries.EditItem;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;

import com.example.boyko.mike.groceries.IntentConstants;
import com.example.boyko.mike.groceries.R;
import com.example.boyko.mike.groceries.db.models.Category;
import com.example.boyko.mike.groceries.db.models.ListItemComplete;
import com.example.boyko.mike.groceries.db.models.QuantityType;
import com.example.boyko.mike.groceries.repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public final static String ACTION_TAG = "action";

    public final static String ACTION_UPDATE = "update";
    public final static String ACTION_DELETE = "delete";
    public final static String ACTION_CANCEL = "cancel";

    // The ListItem being edited
    private ListItemComplete listItem;

    private EditItemViewModel editItemViewModel;
    private QuantityTypeAdapter quantityTypeAdapter;
    private CategoryAdapter categoryAdapter;

    private EditText name;
    private TextView quantity;
    private Spinner quantityType;
    private Spinner category;
    private CheckBox coupon;
    private EditText notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);


        name = (EditText) findViewById(R.id.itemName);
        quantity = (TextView) findViewById(R.id.quantity);

        editItemViewModel = ViewModelProviders.of(this).get(EditItemViewModel.class);

        quantityType = (Spinner) findViewById(R.id.quantityType);
        quantityTypeAdapter = new QuantityTypeAdapter(this, android.R.layout.simple_spinner_item);
        quantityTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantityType.setAdapter(quantityTypeAdapter);
        // Spinner click listener
        quantityType.setOnItemSelectedListener(this);

        category = (Spinner) findViewById(R.id.category);
        categoryAdapter = new CategoryAdapter(this, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);
        // Spinner click listener
        category.setOnItemSelectedListener(this);

        editItemViewModel.getQuantityTypes().observe(this, new Observer<List<QuantityType>>() {
            @Override
            public void onChanged(@Nullable List<QuantityType> types) {
                quantityTypeAdapter.setQuantityTypes(types);

                // With the types now in place, set the quantity type if it was set before.
                if (listItem.listItem.quantityTypeId != null) {
                    for (int i=0; i<quantityTypeAdapter.getCount(); i++) {
                        if (((QuantityType)quantityTypeAdapter.getItem(i)).id == listItem.listItem.quantityTypeId) {
                            quantityType.setSelection(i);
                        }
                    }
                }
            }
        });

        editItemViewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                categoryAdapter.setCategories(categories);

                // With the categories now in place, set the category if it was set
                if (listItem.listItem.categoryId != null) {
                    for (int i = 0; i < categoryAdapter.getCount(); i++) {
                        if (((Category) categoryAdapter.getItem(i)).id == listItem.listItem.categoryId) {
                            category.setSelection(i);
                        }
                    }
                }
            }
        });

        coupon = (CheckBox) findViewById(R.id.coupon);
        notes = (EditText) findViewById(R.id.notes);

        // If there is an intent, then fill in the listItem
        Intent intent = getIntent();
        if (intent != null) {
            listItem = intent.getParcelableExtra(ListItemComplete.TAG);

            name.setText(listItem.listItem.name);
            quantity.setText(String.format(Locale.US, "%d", listItem.listItem.quantity));
            if (listItem.listItem.quantityTypeId != null) {

                // Loop through the quantity types because quantityTypeAdapter.getPosition is calling
                // toString() under the covers for it's comparison and never finds the object.
                for (int i=0; i<quantityTypeAdapter.getCount(); i++) {
                    if (((QuantityType)quantityTypeAdapter.getItem(i)).id == listItem.listItem.quantityTypeId) {
                        quantityType.setSelection(i);
                    }
                }
            }

            if (listItem.category != null) {
                // Loop through the categories because categoryArrayAdapter.getPosition is calling
                // toString() under the covers for it's comparison and never finds the object.
                for (int i=0; i<categoryAdapter.getCount(); i++) {
                    if (((Category)categoryAdapter.getItem(i)).id == listItem.category.id) {
                        category.setSelection(i);
                    }
                }
            }

            coupon.setChecked(listItem.listItem.coupon);
            notes.setText(listItem.listItem.notes);
        }
        else {
            listItem = new ListItemComplete("Unknown");
        }

    }


    public void increaseQuantity(View view) {
        listItem.listItem.quantity++;
        quantity.setText(String.format(Locale.US,"%d", listItem.listItem.quantity));
    }


    public void decreaseQuantity(View view) {

        // Ensure that the quantity cannot become nothing or empty.
        if (listItem.listItem.quantity <= 1) {
            listItem.listItem.quantity = 1;
        }
        else {
            listItem.listItem.quantity--;
        }
        quantity.setText(String.format(Locale.US,"%d", listItem.listItem.quantity));
    }

    /**
     * Save the current ListItem and leave the activity.
     *
     * @param v
     */
    public void saveItem(View v) {

        // Update the ListItem object
        listItem.listItem.name = name.getText().toString();
        listItem.listItem.quantity = Integer.parseInt(quantity.getText().toString());
        listItem.quantityType = (QuantityType)quantityType.getSelectedItem();
        if (listItem.quantityType != null) {

            // Special case: if quantity type is "None"
            if (listItem.quantityType.single.equals(QuantityType.NONE)) {
                listItem.quantityType = null;
                listItem.listItem.quantityTypeId = null;
            }
            else {
                listItem.listItem.quantityTypeId = new Long(listItem.quantityType.id);
            }
        }
        listItem.category = (Category)category.getSelectedItem();
        if (listItem.category != null) {

            // Special case: if the category is "Uncategorized"
            if (listItem.category.name.equals(Category.UNCATEGORIZED)) {
                listItem.category = null;
                listItem.listItem.categoryId = null;
            }
            else {
                listItem.listItem.categoryId = new Long(listItem.category.id);
            }
        }
        listItem.listItem.coupon = coupon.isChecked();
        listItem.listItem.notes = notes.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(ListItemComplete.TAG, listItem);
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
        intent.putExtra(ListItemComplete.TAG, listItem);
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
