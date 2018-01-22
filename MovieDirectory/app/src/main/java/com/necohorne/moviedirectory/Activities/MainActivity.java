package com.necohorne.moviedirectory.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.necohorne.moviedirectory.Data.MovieRecyclerViewAdapter;
import com.necohorne.moviedirectory.Model.Movie;
import com.necohorne.moviedirectory.R;
import com.necohorne.moviedirectory.Util.Constants;
import com.necohorne.moviedirectory.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_main );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        queue = Volley.newRequestQueue(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        Prefs prefs = new Prefs( MainActivity.this);
        String search = prefs.getSearch();

        movieList = new ArrayList<>();

        //getMovies(search);
        movieList = getMovies(search);

        //the below lines of code sets up the recycler view
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setAdapter( movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_search) {
            showInputDialog();
            //return true;
        }

        return super.onOptionsItemSelected( item );
    }

    public void showInputDialog(){

        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        final EditText newSearchEdt = (EditText) view.findViewById( R.id.searchEdt);
        Button submitButton = (Button) view.findViewById( R.id.submitButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        submitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs = new Prefs( MainActivity.this );

                if (!newSearchEdt.getText().toString().isEmpty()) {

                    String search = newSearchEdt.getText().toString();
                    prefs.setSearch(search);
                    movieList.clear();

                    getMovies(search);

                   // movieRecyclerViewAdapter.notifyDataSetChanged(); //very import to update this
                }
                dialog.dismiss();
            }
        } );

    }

    public List<Movie> getMovies(String searchTerm) {
        movieList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET,
                Constants.URL_LEFT + searchTerm + Constants.URL_RIGHT + Constants.API_KEY, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray movieArray = response.getJSONArray("Search");

                    for (int i = 0; i < movieArray.length(); i++){
                        JSONObject movieObject = movieArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setTitle(movieObject.getString("Title"));
                        movie.setYear("Year Released: " + movieObject.getString("Year"));
                        movie.setMovieType("Type: " + movieObject.getString("Type"));
                        movie.setPoster( movieObject.getString("Poster"));
                        movie.setImdbId( movieObject.getString("imdbID"));

                        // Log.d("Movies: ", movie.getTitle()); its working

                        movieList.add(movie);
                    }
                    /**
                     * Very Important, otherwise nothing will display.
                     */
                    movieRecyclerViewAdapter.notifyDataSetChanged();

                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add( jsonObjectRequest);
        return movieList;
    }
}
