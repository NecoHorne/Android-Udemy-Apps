package com.necohorne.honeydolist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private Button saveButton;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        editText = (EditText) findViewById( R.id.toDoList );

        saveButton = (Button) findViewById( R.id.saveButton );
        saveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals( "")){
                    String message = editText.getText().toString();
                    writeToFile( message);
                }else {
                    Toast.makeText( MainActivity.this, "Enter something", Toast.LENGTH_LONG).show();
                }

            }
        } );

        try {
            if (readFromFile() != null) {
                editText.setText(readFromFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeToFile(String message) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput( "todolist.txt", Context.MODE_PRIVATE ));
            outputStreamWriter.write( message );
            outputStreamWriter.close(); // always close streams!
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFromFile() throws IOException {
        String result = "";

        InputStream inputStream = openFileInput( "todolist.txt" );

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader( inputStreamReader );
            String tempString = "";
            StringBuilder sb = new StringBuilder();
            while ((tempString = bufferedReader.readLine()) != null) {
                sb.append( tempString );
            }
            inputStream.close();
            result = sb.toString();
        }
        return result;
    }

}
