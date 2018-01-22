package com.necohorne.mygrocerylist.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.necohorne.mygrocerylist.R;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView itemNameDET;
    private TextView itemQuantityDET;
    private TextView itemDateDET;
    private Button editButtonDET;
    private Button deleteButtonDET;
    private int groceryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_details );

        itemNameDET = (TextView) findViewById( R.id.itemNameDET );
        itemQuantityDET = (TextView) findViewById( R.id.itemQuantityDET);
        itemDateDET = (TextView) findViewById( R.id.itemDateDET );

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            itemNameDET.setText(bundle.getString("name"));
            itemQuantityDET.setText(bundle.getString("quantity"));
            itemDateDET.setText(bundle.getString("date"));
            groceryId = bundle.getInt( "id" );
        }

        editButtonDET = (Button) findViewById(R.id.editButtonDET);
        deleteButtonDET = (Button) findViewById(R.id.deleteButton);

        editButtonDET.setOnClickListener(this);
        deleteButtonDET.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editButton:
                //TODO
                break;

            case R.id.deleteButton:
                //TODO
                break;
        }

    }
}
