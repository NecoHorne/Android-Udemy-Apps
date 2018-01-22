package com.necohorne.petbio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BioActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView nameText;
    private TextView textBio;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_bio );

        imageView = (ImageView) findViewById( R.id.petBioImageId );
        textBio = (TextView) findViewById( R.id.textBioID );
        nameText = (TextView) findViewById( R.id.nameId );

        extras = getIntent().getExtras();

        if (extras != null) {
            String name = extras.getString("name");
            String bio = extras.getString("bio" );

            setUp(name, bio);
        }

    }
    public void setUp(String name, String bio) {
        if (name.equals("Dufus")) {
            //show dog
            imageView.setImageDrawable(getResources().getDrawable( R.drawable.icon_lg_dog));
            nameText.setText(name);
            textBio.setText(bio);
        } else if (name.equals( "Jarvis" )){
            //show cat
            imageView.setImageDrawable(getResources().getDrawable( R.drawable.icon_lg_cat));
            nameText.setText(name);
            textBio.setText(bio);
        }

    }
}
