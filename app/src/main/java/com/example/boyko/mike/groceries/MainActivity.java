package com.example.boyko.mike.groceries;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.view.KeyEvent;
import android.content.Intent;

import java.util.List;
import java.util.concurrent.Callable;

import com.example.boyko.mike.groceries.db.models.Category;
import com.example.boyko.mike.groceries.db.models.InventoryItem;
import com.example.boyko.mike.groceries.db.models.ListItem;


public class MainActivity extends AppCompatActivity {

    public final static String POSITION_TAG = "Postition Tag";
    public final static int ILLEGAL_POSITION = -1;

    // This keeps the listView data separate from the View. Layers...
    private ListItemViewModel listItemViewModel;
    private CategoryViewModel categoryViewModel;

    private InventoryItemViewModel inventoryItemViewModel;

    // Variables associated with a list view
    private ListView listView;
    private ItemArrayAdapter arrayAdapter;

    // Variables associated with the "Add an listItem" edit text box
    private EditText newItem;
    private List<InventoryItem> inventoryItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listItemViewModel = ViewModelProviders.of(this).get(ListItemViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        inventoryItemViewModel = ViewModelProviders.of(this).get(InventoryItemViewModel.class);

        listView = (ListView) findViewById(R.id.listView);

        // Add a listener for clicks on the Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListItem clickedListItem = arrayAdapter.getListItem(position);

                // Only do something if the User clicked on a List ListItem and not a header.
                if (clickedListItem != null) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, EditItemActivity.class);
                    intent.putExtra(ListItem.TAG, clickedListItem);
                    intent.putExtra(MainActivity.POSITION_TAG, position);
                    startActivityForResult(intent, IntentConstants.EDIT_ITEM);
                }
            }
        });


        arrayAdapter = new ItemArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
        // initializeListView( );

        listItemViewModel.getListItems().observe(MainActivity.this, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(@Nullable List<ListItem> listItems) {
                arrayAdapter.setItems(listItems);
            }
        });

        categoryViewModel.getCategories().observe(MainActivity.this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                 arrayAdapter.setCategories(categories);
            }
        });

        inventoryItemViewModel.getInventoryItems().observe(MainActivity.this, new Observer<List<InventoryItem>>() {
            @Override
            public void onChanged(@Nullable List<InventoryItem> changedInventoryItems) {
                inventoryItems = changedInventoryItems;
            }
        });

        newItem = (EditText) findViewById(R.id.newItem);
        newItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.i("Groceries", "An editor action occured.");
                if (keyEvent == null) {
                    if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {

                        // Grab the text
                        String input = newItem.getText().toString();

                        InventoryItem inventoryItem = getInventoryItem(input);

                        if (inventoryItem ==  null) {
                            inventoryItem = new InventoryItem(input);
                            inventoryItemViewModel.addInventoryItem(inventoryItem);
                        }

                        ListItem item = new ListItem(inventoryItem.name);
                        item.categoryId = inventoryItem.id;

                        // Add the listItem to the list
                        listItemViewModel.addItem(item);
                        // addInputToList(input);

                        // Clear out the text box
                        newItem.setText("");

                        // Return false to ensure that the keyboard will close.
                        return false;
                    }
                    else {
                        return false;
                    }
                }
                else if (i == EditorInfo.IME_NULL) {
                    // Consume the event.
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar listItem clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // We only care about the edit activity.
        if (requestCode != IntentConstants.EDIT_ITEM) {
            return;
        }

        String action = data.getStringExtra(EditItemActivity.ACTION_TAG);
        int position = -1;

        switch(action) {
            case EditItemActivity.ACTION_UPDATE:
                ListItem listItem = data.getParcelableExtra(ListItem.TAG);
                position = data.getIntExtra(MainActivity.POSITION_TAG, -1);

                if (position == MainActivity.ILLEGAL_POSITION) {
                    Log.e("Groceries", "Illegal intent position detected.");
                    return;
                }

                arrayAdapter.updateItem(position, listItem);
                break;

            case EditItemActivity.ACTION_DELETE:
                position = data.getIntExtra(MainActivity.POSITION_TAG, -1);

                if (position == MainActivity.ILLEGAL_POSITION) {
                    Log.e("Groceries", "Illegal intent position detected.");
                    return;
                }

                arrayAdapter.deleteItem(position);
                break;

            case EditItemActivity.ACTION_CANCEL:
                // The user cancelled the edit, there is nothing to do here.
                break;

            default:
                Log.e("Groceries", "Illegal intent action detected (" + action + ").");
        }
    }


    /**
     * Temporarily seed the ListView with Items.
     */
    /*
    private void initializeListView( ) {

        String[] strings = new String[]{
          "Cheese",
          "Milk",
          "Oranges",
          "Bananas",
          "Corn"
        };

        for ( String str: strings) {
            addInputToList(str);
        }
    }
    */


    /**
     * Add a new ListItem to the ListItem list.
     *
     * @param name The ListItem name
     */
    /*
    private void addInputToList (String name) {

        ListItem listItem = new ListItem(name);
        arrayAdapter.addItem(listItem);
        Log.i("Groceries", "Adding " + listItem.toString());
    }
    */


    /**
     * Get the Inventory Item with the given name.
     *
     * @param name The name of the Inventory Item to find.
     * @return InventoryItem if found, null otherwise.
     */
    private InventoryItem getInventoryItem(String name) {
        for (InventoryItem item: inventoryItems) {
            if (item.name.equals(name)) {
                return item;
            }
        }
        return null;
    }
}
