package com.necohorne.volleyparsing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final static String URL_STRING = "http://magadistudio.com/complete-android-developer-course-source-files/string.html";
    private final static String URL = "https://jsonplaceholder.typicode.com/posts";
    private final static String URL_EQ = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/1.0_hour.geojson";

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        queue = Volley.newRequestQueue( this);

        getStringObject(URL_STRING);
        getJSONArray(URL);
        getJSONObject(URL_EQ);
    }

    private void getJSONObject(String url){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d( "Object: " , response.getString( "type" ) );

                            JSONObject metaData = response.getJSONObject("metadata");
                            Log.d( "Metadata: ", metaData.toString() );
                            Log.d( "Metainfo: ", metaData.getString( "generated" ) );
                            Log.d( "Metainfo: ", metaData.getString( "url" ) );
                            Log.d( "Metainfo: ", metaData.getString( "title" ) );
                            Log.d( "Metainfo: ", metaData.getString( "status" ) );
                            Log.d( "Metainfo: ", metaData.getString( "api" ) );
                            Log.d( "Metainfo: ", metaData.getString( "count" ) );

                            JSONArray features = response.getJSONArray( "features");

                            for (int i = 0; i < features.length(); i++) {
                                //get objects
                                JSONObject propertiesObj = features.getJSONObject(i).getJSONObject("geometry");

                                Log.d( "Place: ", propertiesObj.getString( "coordinates"));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add( jsonObjectRequest);
    }

    public void getJSONArray(String url){

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject movieObject = response.getJSONObject(i);
                        Log.d("Items: ", movieObject.getString( "title") + "/ "
                                + "ID: " + movieObject.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Log.d("Response: ", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", error.getMessage());
            }
        });
        queue.add(arrayRequest);
    }

    public void getStringObject(String url) {

        StringRequest stringRequest = new StringRequest( Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("My String: ", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        } );
        queue.add(stringRequest);
    }

}
