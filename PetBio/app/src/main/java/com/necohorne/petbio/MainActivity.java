package com.necohorne.petbio;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView catImage;
    private ImageView dogImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        catImage = (ImageView) findViewById( R.id.catImageId );
        dogImage = (ImageView) findViewById( R.id.dogImageId );

        catImage.setOnClickListener(this);
        dogImage.setOnClickListener(this);

    }

    // for multiple buttons have the main activity implement View.onClickListener and add the below method.
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.catImageId:
                //code for cat click, go to a new activity.
                //Toast.makeText( MainActivity.this, "Cat Clicked", Toast.LENGTH_SHORT).show();

                Intent catintent = new Intent( MainActivity.this, BioActivity.class );
                catintent.putExtra("name", "Jarvis");
                catintent.putExtra("bio", "Great cat, loves people and meows a lot");
                startActivity(catintent);

                break;

            case R.id.dogImageId:
                //code for dog click, go to a new activity.
                //Toast.makeText( MainActivity.this, "dog Clicked", Toast.LENGTH_SHORT ).show();

                Intent dogintent = new Intent( MainActivity.this, BioActivity.class );
                dogintent.putExtra("name", "Dufus");
                dogintent.putExtra("bio", "Great dog, loves people and barks and eats a lot");
                startActivity(dogintent);

                break;
        }
    }
}
