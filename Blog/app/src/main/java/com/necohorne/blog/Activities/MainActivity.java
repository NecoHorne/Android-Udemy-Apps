package com.necohorne.blog.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.necohorne.blog.R;

public class MainActivity extends AppCompatActivity {

    //UI Elements
    private EditText emailField;
    private EditText passwordField;
    private Button signInButton;
    private Button createActButton;

    //Firebase Elements
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        setUpUI();
        setUpEmailAuth();
    }

    private void setUpUI(){

        emailField = findViewById(R.id.emailText);
        passwordField = findViewById(R.id.passwordText);
        signInButton = findViewById(R.id.signInButton);
        createActButton = findViewById( R.id.createActButton);

        createActButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( MainActivity.this, CreateAccountActivity.class));
                finish();
            }
        } );

    }

    private void setUpEmailAuth(){

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    Toast.makeText( MainActivity.this, "Signed In", Toast.LENGTH_LONG ).show();
                    startActivity(new Intent( MainActivity.this, PostListActivity.class));
                    finish();
                } else {
                    Toast.makeText( MainActivity.this, "Not Signed In", Toast.LENGTH_LONG ).show();
                }
            }
        };

        signInButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailField.getText().toString()) && !TextUtils.isEmpty( passwordField.getText().toString())) {
                    //if the email and password fields are not empty, try to log in

                    String email = emailField.getText().toString();
                    String password = passwordField.getText().toString();

                    logInUser(email, password);

                } else {
                    //if the email and password fields are empty, do not attempt login
                    
                }
            }
        } );
    }

    private void logInUser(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Log.d( "User Signed in: ", "user " + email + " signed in:");
                    Toast.makeText(MainActivity.this, "Signed In",
                            Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    startActivity( new Intent( MainActivity.this, PostListActivity.class));
                    finish();

                } else {

                    Log.d( "Sign in failed", "user " + email + " Sign in failed");
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_LONG).show();
                    //updateUI(null);
                }

            }
        } );

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_signout) {
            mAuth.signOut();
            Toast.makeText( this, "Signed Out", Toast.LENGTH_LONG ).show();
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main_menu,menu);
        return super.onCreateOptionsMenu( menu );
    }
}
