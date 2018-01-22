package com.necohorne.blog.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.necohorne.blog.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    //UI Variables
    private EditText firstName;
    private EditText lastName;
    private EditText mEmail;
    private EditText mPassword;
    private Button createActButton;
    private ProgressDialog progressDialog;
    private ImageButton profilePicture;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    //gallery variables
    private Uri mImageUri;
    private static final int GALLERY_CODE = 1;
    private Uri resultUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_create_account );

        setUpUI();
        setUpFirebase();
    }

    private void setUpUI(){

        firstName = findViewById( R.id.firstNameAct );
        lastName = findViewById( R.id.lastNameAct );
        mEmail = findViewById( R.id.emailAct );
        mPassword = findViewById( R.id.passwordAct );

        progressDialog = new ProgressDialog( this );

        createActButton = findViewById(R.id.createActButtonact);
        createActButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        } );

        profilePicture = findViewById( R.id.profilePictureId);
        profilePicture.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfilePicture();
            }
        } );
    }

    private void setUpFirebase(){

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child( "mUsers" );

        //Firebase Authorization
        mAuth = FirebaseAuth.getInstance();

        //Firebase Storage
        mStorageRef = FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics");
    }

    private void createNewAccount() {

        final String name = firstName.getText().toString().trim();
        final String surname = lastName.getText().toString().trim();
        final String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            //fields are not empty, new account can be created

            progressDialog.setMessage( "Creating Account...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword( email, password ).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        // account has been created

                        Log.d("account creation", "createUserWithEmail:success");

                        final StorageReference filePath = mStorageRef.child("MBlog_Profile_Pics").child(resultUri.getLastPathSegment());
                        filePath.putFile(resultUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = mDatabaseReference.child(userId);
                                currentUserDb.child( "firstname" ).setValue(name);
                                currentUserDb.child( "lastname" ).setValue(surname);
                                currentUserDb.child( "image" ).setValue(resultUri.toString());
                                currentUserDb.child( "email" ).setValue(email);

                                progressDialog.dismiss();

                                Intent intent = new Intent( CreateAccountActivity.this, PostListActivity.class);
                                intent.addFlags( intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                    }else {
                        Log.d("account creation", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(CreateAccountActivity.this, "Account Creation failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            //one or more fields are empty, account creation failed.
            //todo
          }
        }

    private void addProfilePicture() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1 )
                    .setGuidelines( CropImageView.Guidelines.ON)
                    .start(CreateAccountActivity.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                resultUri = result.getUri();
                profilePicture.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

}
