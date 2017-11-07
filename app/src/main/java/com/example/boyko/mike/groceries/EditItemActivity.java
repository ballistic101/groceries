package com.example.boyko.mike.groceries;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;

import java.util.Locale;

public class EditItemActivity extends AppCompatActivity {

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
        // @todo - Add an array adapter to give this options, and then initialize to the one
        // item is using.

        category = (Spinner) findViewById(R.id.category);
        // @todo - Add an array adapter to give this options, and then initialize to the one
        // item is using.

        coupon = (CheckBox) findViewById(R.id.coupon);
        notes = (EditText) findViewById(R.id.notes);

        // If there is an intent, then fill in the item
        Intent intent = getIntent();
        if (intent != null) {
            item = intent.getParcelableExtra(Item.TAG);
            position = intent.getIntExtra(MainActivity.POSITION_TAG, MainActivity.ILLEGAL_POSITION);

            name.setText(item.name);
            quantity.setText(String.format(Locale.US, "%d", item.quantity));
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
        item.quantityType = quantityType.toString();
        item.category = category.toString();
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
}
