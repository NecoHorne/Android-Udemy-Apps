package com.necohorne.earthquakewatcher.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.necohorne.earthquakewatcher.Model.EarthQuake;
import com.necohorne.earthquakewatcher.R;
import com.necohorne.earthquakewatcher.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuakesListActivity extends AppCompatActivity {

    private ArrayList<String> arrayList;
    private ListView listView;
    private RequestQueue queue;
    private ArrayAdapter arrayAdapter;

    private List<EarthQuake> quakeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_quakes_list );

        quakeList = new ArrayList<>();
        listView = (ListView) findViewById( R.id.listView );
        queue = Volley.newRequestQueue( this);
        arrayList = new ArrayList<>();

        getAllQuakes(Constants.URL);
    }

    public void getAllQuakes(String url) {

        final EarthQuake earthQuake = new EarthQuake();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("features");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject properties = jsonArray.getJSONObject(i).getJSONObject("properties");
                        JSONObject geometry = jsonArray.getJSONObject(i).getJSONObject("geometry");

                        //get the Coordinates from the Json Array within the geometry object
                        JSONArray coordinates = geometry.getJSONArray("coordinates");
                        double longitude = coordinates.getDouble(0 );
                        double latitude = coordinates.getDouble(1);

                        //set up the EarthQuake object with the Json data.
                        earthQuake.setMag(properties.getDouble("mag"));
                        earthQuake.setPlace(properties.getString("place"));

                        earthQuake.setTime(properties.getLong("time"));
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                        String formattedDate = dateFormat.format( new Date(earthQuake.getTime()).getTime());

                        earthQuake.setUrl(properties.getString("url"));
                        earthQuake.setDetail(properties.getString("detail"));
                        earthQuake.setMagType(properties.getString("magType"));
                        earthQuake.setType(properties.getString("type"));
                        earthQuake.setLongitude(longitude);
                        earthQuake.setLatitude(latitude);

                        arrayList.add( earthQuake.getPlace());

                    }

                    arrayAdapter = new ArrayAdapter<>( QuakesListActivity.this, android.R.layout.simple_list_item_1,
                    android.R.id.text1, arrayList);
                    listView.setAdapter( arrayAdapter);
                    listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getApplicationContext(),"Clicked "+ position, Toast.LENGTH_LONG).show();
                        }
                    } );
                    arrayAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }
}
