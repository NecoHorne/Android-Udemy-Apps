package com.necohorne.tipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private TextView result;
    private EditText editText;
    private SeekBar seekBar;
    private TextView percentText;
    private int seekBarPercentage;
    private float enteredBillFloat;
    private TextView totalBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        button = (Button) findViewById( R.id.button );
        result = (TextView) findViewById( R.id.resultId );
        editText = (EditText) findViewById( R.id.billAmountId );
        seekBar = (SeekBar) findViewById( R.id.seekBarId );
        percentText = (TextView) findViewById( R.id.showPercentage );
        totalBill = (TextView) findViewById( R.id.totalBill );

        button.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percentText.setText( String.valueOf(seekBar.getProgress()) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarPercentage = seekBar.getProgress();
            }
        } );

    }

    @Override
    public void onClick(View v) {
        calculate();
    }

    public void calculate(){
        float tipResult = 0.0f;
        float totalResult = 0.0f;

        if (!editText.getText().toString().equals( "")) {
            enteredBillFloat = Float.parseFloat(editText.getText().toString());
            tipResult = enteredBillFloat * seekBarPercentage / 100;
            result.setText("Your tip will be : " + String.valueOf(tipResult));
            totalResult = enteredBillFloat + tipResult;
            totalBill.setText("Your Total bill will be : " + String.valueOf(totalResult));
        }else {
            Toast.makeText( MainActivity.this, "Please Enter a Bill Amount", Toast.LENGTH_LONG).show();
        }


    }
}
