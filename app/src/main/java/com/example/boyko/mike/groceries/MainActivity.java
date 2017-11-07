package com.example.boyko.mike.groceries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.view.KeyEvent;
import android.content.Intent;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    public final static String POSITION_TAG = "Postition Tag";
    public final static int ILLEGAL_POSITION = -1;

    // Variables associated with a list view
    ListView listView;
    ArrayList<Item> arrayList;
    ItemArrayAdapter arrayAdapter;

    // Variables associated with the "Add an item" edit text box
    EditText newItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);

        // Add a listener for clicks on the Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EditItemActivity.class);
                intent.putExtra(Item.TAG, arrayList.get(position));
                intent.putExtra(MainActivity.POSITION_TAG, position);
                startActivityForResult(intent, IntentConstants.EDIT_ITEM);
            }
        });

        arrayList = new ArrayList<Item>();

        initializeArrayList( arrayList );
        arrayAdapter = new ItemArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList, this);
        listView.setAdapter(arrayAdapter);

        newItem = (EditText) findViewById(R.id.newItem);
        newItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.i("Groceries", "An editor action occured.");
                if (keyEvent == null) {
                    if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {

                        // Grab the text
                        String input = newItem.getText().toString();

                        // Add the item to the list
                        addInputToList(input);

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
        // Handle action bar item clicks here. The action bar will
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
                Item item = data.getParcelableExtra(Item.TAG);
                position = data.getIntExtra(MainActivity.POSITION_TAG, -1);

                if (position == MainActivity.ILLEGAL_POSITION) {
                    Log.e("Groceries", "Illegal intent position detected.");
                    return;
                }

                // Update the arrayList with the altered item
                arrayList.set(position, item);

                // Let the adapter know that the data has changed
                arrayAdapter.notifyDataSetChanged();
                break;

            case EditItemActivity.ACTION_DELETE:
                position = data.getIntExtra(MainActivity.POSITION_TAG, -1);

                if (position == MainActivity.ILLEGAL_POSITION) {
                    Log.e("Groceries", "Illegal intent position detected.");
                    return;
                }

                arrayList.remove(position);

                // Let the adapter know that the data has changed
                arrayAdapter.notifyDataSetChanged();
                break;

            case EditItemActivity.ACTION_CANCEL:
                // The user cancelled the edit, there is nothing to do here.
                break;

            default:
                Log.e("Groceries", "Illegal intent action detected (" + action + ").");
        }
    }


    /**
     * Temporarily seed the arrayList with Items.
     *
     * @param arrayList The array list to populate.
     */
    private void initializeArrayList( ArrayList<Item> arrayList) {

        String[] strings = new String[]{
          "Cheese",
          "Milk",
          "Oranges",
          "Bananas",
          "Corn"
        };

        for ( String str: strings) {
            Item item = new Item(str);
            arrayList.add(item);
        }
    }


    /**
     * Add a new Item to the Item list.
     *
     * @param name The Item name
     */
    private void addInputToList (String name) {

        Item item = new Item(name);
        arrayList.add(item);
        Log.i("Groceries", "Adding " + item.toString());
    }

}
