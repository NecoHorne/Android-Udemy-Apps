package com.necohorne.mygrocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.necohorne.mygrocerylist.Model.Grocery;
import com.necohorne.mygrocerylist.R;
import com.necohorne.mygrocerylist.UI.RecyclerViewAdapter;
import com.necohorne.mygrocerylist.data.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private DatabaseHandler db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopUpDialog();
            }
        } );

        db = new DatabaseHandler(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItems = new ArrayList<>();

        //get the items from the database.
        groceryList = db.getAllGroceries();

        for( Grocery c : groceryList) {
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity("Qty: " + c.getQuantity());
            grocery.setId(c.getId());
            grocery.setDateItemAdded("Added on: " + c.getDateItemAdded());

            listItems.add(grocery);
        }
        recyclerViewAdapter = new RecyclerViewAdapter( this, listItems);
        recyclerView.setAdapter( recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void createPopUpDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.pop_up, null);
        groceryItem = (EditText) view.findViewById( R.id.groceryItem);
        quantity = (EditText) view.findViewById( R.id.groceryQty );
        saveButton = (Button) view.findViewById( R.id.saveButton );

        dialogBuilder.setView( view );
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty()) {
                    saveGroceryToDB(v);
                }
                dialog.dismiss();
            }
        } );
    }

    private void saveGroceryToDB(View v) {

        Grocery grocery = new Grocery();


        String newGrocery  = groceryItem.getText().toString();
        String newGroceryQuantity = quantity.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQuantity);

        //save to db
        db.addGrocery(grocery);
        recyclerViewAdapter.notifyDataSetChanged();

        Snackbar.make( v, "Item Saved", Snackbar.LENGTH_LONG).show();
        //Log.d( "Item Added, Count: ", String.valueOf(db.getGroceryCount()));

        }
    }
