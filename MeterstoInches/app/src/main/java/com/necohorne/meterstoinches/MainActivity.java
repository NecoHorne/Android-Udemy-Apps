package com.necohorne.meterstoinches;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private EditText meters;
    private TextView centiMeters;
    private TextView milliMeters;
    private TextView inches;
    private TextView feet;
    private Button convertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        meters = (EditText) findViewById(R.id.editText);
        centiMeters = findViewById( R.id.CentimeterID );
        milliMeters = findViewById( R.id.milliMeterID );
        inches = (TextView) findViewById(R.id.inchResult);
        feet = (TextView) findViewById(R.id.feetTextID);
        convertButton = (Button) findViewById(R.id.convertID);

        convertButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double inchConverter = 39.3701;

                if (meters.getText().toString().equals("")){
                    centiMeters.setText(R.string.error_message);
                    centiMeters.setTextColor(Color.RED);
                    centiMeters.setTextSize(20);
                    centiMeters.setVisibility(View.VISIBLE);
                    milliMeters.setVisibility(View.INVISIBLE);
                    inches.setVisibility(View.INVISIBLE);
                    feet.setVisibility(View.INVISIBLE);

                }else
                    {
                    double enteredValue = Double.parseDouble( meters.getText().toString());

                    double centiMeterValue = enteredValue * 100;
                    double milliMeterValue = enteredValue * 1000;

                    double inchResult = enteredValue * inchConverter;

                    double feetConverter = 12;
                    double feetValue = inchResult / feetConverter;

                    centiMeters.setText(String.format("%.2f", centiMeterValue) + " cm");
                    centiMeters.setVisibility(View.VISIBLE);
                    centiMeters.setTextColor(Color.BLACK);
                    centiMeters.setTextSize(14);

                    milliMeters.setText(String.format("%.2f", milliMeterValue) + " mm");
                    milliMeters.setVisibility(View.VISIBLE);
                    milliMeters.setTextColor(Color.BLACK);

                    inches.setText(String.format("%.2f", inchResult) + " Inches.");
                    inches.setVisibility(View.VISIBLE);
                    inches.setTextColor(Color.BLACK);

                    feet.setText(String.format( "%.2f", feetValue) + " Feet.");
                    feet.setVisibility(View.VISIBLE);
                    feet.setTextColor(Color.BLACK);
                    //feet.setText(Double.toString(feetValue) + " Feet");
                }
            }
        } );

    }
}
