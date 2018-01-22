package com.necohorne.blog.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.necohorne.blog.Model.Blog;
import com.necohorne.blog.R;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    // UI variables
    private ImageButton mPostImage;
    private EditText mTitle;
    private EditText mDescription;
    private Button submitButton;
    private ProgressDialog mProgress;

    //FireBase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    //gallery variables
    private Uri mImageUri;
    private static final int GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_post );

        setUpUI();
        firebaseInit();

    }

    public void setUpUI(){

        mPostImage = findViewById( R.id.imageButton);
        mTitle = findViewById( R.id.titleEditText);
        mDescription = findViewById( R.id.descriptionEditText );
        submitButton = findViewById( R.id.submitPost );
        mProgress = new ProgressDialog(this);

        submitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        } );

        mPostImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFromGallery();
            }
        } );

    }

    public void firebaseInit(){

        //Current User
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Current Database
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("MBlog");

        //cloud storage
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void startPosting() {

        mProgress.setMessage( "Posting to Blog..." );
        mProgress.show();

        final String titleVal = mTitle.getText().toString().trim();
        final String descVal = mDescription.getText().toString().trim();

        if (!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal) && mImageUri != null) {
            //start upload.
            //mImageUri.getLastPathSegment() == ex. /image/myphoto.jpeg

            final StorageReference filePath = mStorageRef.child("MBlog_images").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = mDatabaseReference.push();

                    Map<String, String> dataToSave = new HashMap<>();
                    //string values need to be exactly the same as the variables in the model class.
                    dataToSave.put("title", titleVal);
                    dataToSave.put("description", descVal);
                    dataToSave.put("image", downloadUrl.toString());
                    dataToSave.put("timeStamp", String.valueOf(java.lang.System.currentTimeMillis()));
                    dataToSave.put("userID", mUser.getUid());

                    newPost.setValue(dataToSave);
                    mProgress.dismiss();

                    startActivity( new Intent(  AddPostActivity.this, PostListActivity.class));
                    finish();
                }
            } );

        }
    }

    public void imageFromGallery(){

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mPostImage.setImageURI(mImageUri);

        }
    }
}
