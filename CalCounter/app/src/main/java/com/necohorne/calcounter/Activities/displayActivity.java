package com.necohorne.calcounter.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.necohorne.calcounter.Model.Food;
import com.necohorne.calcounter.R;
import com.necohorne.calcounter.Util.Utils;
import com.necohorne.calcounter.data.CustomListViewAdapter;
import com.necohorne.calcounter.data.DatabaseHandler;

import java.util.ArrayList;

public class displayActivity extends AppCompatActivity {

    private DatabaseHandler dba;
    private ArrayList<Food> dbFoods = new ArrayList<>();
    private CustomListViewAdapter foodAdapter;
    private ListView listView;
    private Food myFood;
    private TextView totalCals, totalFoods;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_display );

        listView = (ListView) findViewById( R.id.listViewId );
        totalCals = (TextView) findViewById( R.id.totalAmountTextV );
        totalFoods = (TextView) findViewById( R.id.totalItemsTextV );
        
        refreshData();
    }

    private void refreshData() {
        dbFoods.clear();

        dba = new DatabaseHandler(getApplicationContext());

        ArrayList<Food> foodsFromDB = dba.getAllFood();

        int calsValue = dba.totalCalories();
        int totalItems = dba.getTotalItems();

        String formattedValue = Utils.formatNumber(calsValue);
        String formattedItems = Utils.formatNumber(totalItems);

        totalCals.setText( "Total Calories: " + formattedValue);
        totalFoods.setText( "Total Foods: " + formattedItems);

        for (int i = 0; i < foodsFromDB.size(); i++){
            String name = foodsFromDB.get(i).getFoodName();
            String dateText = foodsFromDB.get(i).getRecordDate();
            int cals = foodsFromDB.get(i).getCalories();
            int foodId = foodsFromDB.get(i).getFoodId();

            Log.v( "FOOD ID's", String.valueOf(foodId));

            myFood = new Food();
            myFood.setFoodName(name);
            myFood.setRecordDate(dateText);
            myFood.setCalories(cals);
            myFood.setFoodId(foodId);

            dbFoods.add(myFood);
        }
        dba.close();

        //set up the adapter

        foodAdapter = new CustomListViewAdapter(displayActivity.this, R.layout.list_row,dbFoods);
        listView.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();
    }
}
