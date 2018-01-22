package com.necohorne.calcounter.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.necohorne.calcounter.Model.Food;
import com.necohorne.calcounter.R;
import com.necohorne.calcounter.data.DatabaseHandler;

public class MainActivity extends AppCompatActivity {

    private EditText foodName, foodCals;
    private Button submitButton;
    private DatabaseHandler dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        dba = new DatabaseHandler(MainActivity.this);

        foodName = (EditText) findViewById(R.id.foodEditTextId);
        foodCals = (EditText) findViewById(R.id.calEdittextId);
        submitButton = (Button) findViewById(R.id.submitButtonid);

        submitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToDB();

            }
        } );

    }

    private void saveDataToDB() {
        Food food = new Food();

        String name = foodName.getText().toString().trim();
        String calString = foodCals.getText().toString().trim();

        int cals = Integer.parseInt(calString);

        if (name.equals("") || calString.equals("")) {
            Toast.makeText( getApplicationContext(), "No empty fields allowed", Toast.LENGTH_SHORT ).show();
        }else {
            food.setFoodName(name);
            food.setCalories(cals);

            dba.addFood(food);
            dba.close();

            //clear form

            foodName.setText("");
            foodCals.setText("");

            // take the user to the next screen.
            startActivity( new Intent(MainActivity.this, displayActivity.class));
        }


    }
}
