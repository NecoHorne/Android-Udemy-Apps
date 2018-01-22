package com.necohorne.blog.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.necohorne.blog.Data.BlogRecyclerAdapter;
import com.necohorne.blog.Model.Blog;
import com.necohorne.blog.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PostListActivity extends AppCompatActivity {

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    //View Variables
    private RecyclerView recyclerView;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    private List<Blog> blogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_post_list );

        firebaseInit();
        viewInit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:

                if (mUser != null && mAuth != null) {
                    // check that the user is signed in and authorized, then cont to add activity.
                    startActivity(  new Intent( PostListActivity.this, AddPostActivity.class));
                    finish();
                }
                break;

            case  R.id.action_signout:

                if (mUser != null && mAuth != null) {

                    mAuth.signOut();
                    startActivity(  new Intent( PostListActivity.this, MainActivity.class));
                    finish();

                }

                break;
        }

        return super.onOptionsItemSelected( item );
    }

    public void firebaseInit(){

        //Current User
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Current Database
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MBlog");
        mDatabaseReference.keepSynced(true);
    }

    public void viewInit(){

        blogList = new ArrayList<>();

        recyclerView = findViewById(R.id.recylclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Blog blog = dataSnapshot.getValue(Blog.class);
                blogList.add(blog);
                Collections.reverse(blogList);

                blogRecyclerAdapter = new BlogRecyclerAdapter( PostListActivity.this, blogList);
                recyclerView.setAdapter( blogRecyclerAdapter );
                blogRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

    }
}
