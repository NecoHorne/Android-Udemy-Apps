package com.necohorne.earthquakewatcher.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.necohorne.earthquakewatcher.Model.EarthQuake;
import com.necohorne.earthquakewatcher.R;
import com.necohorne.earthquakewatcher.UI.CustomInfoWindow;
import com.necohorne.earthquakewatcher.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private Button showListBtn;

    //map Variables
    private GoogleMap mMap;
    private BitmapDescriptor[] iconColors;

    //location Variables
    private LocationManager locationManager;
    private LocationListener locationListener;

    //Volley Variables to parse the JSON data from the web
    private RequestQueue queue;

    //Dialog Builder for marker details
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_maps );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );

        queue = Volley.newRequestQueue(this);

        iconColors = new BitmapDescriptor[] {
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET),
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
        };

        getEarthQuakes();

        showListBtn = findViewById( R.id.showListBtn );
        showListBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MapsActivity.this, QuakesListActivity.class));
            }
        } );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setInfoWindowAdapter( new CustomInfoWindow(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener( this );

        setUpLocation();
    }

    private void setUpLocation() {

        locationManager = (LocationManager) this.getSystemService( LOCATION_SERVICE );

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d( "location ", location.toString() );
                mMap.clear();

                LatLng newLocation = new LatLng( location.getLatitude(), location.getLongitude());

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocation( newLocation.latitude, newLocation.longitude, 1 );

                        mMap.addMarker( new MarkerOptions().position(newLocation ).title( addressList.get(0).getAddressLine(0)));
                        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(newLocation, 12) );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        permissionBoilerplate();

        }

    public void permissionBoilerplate(){

        if (ContextCompat.checkSelfPermission( this,
                Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            //Ask User for permission
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
        } else {
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );

        if (grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    public void getEarthQuakes() {

        final EarthQuake earthQuake = new EarthQuake();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET , Constants.URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray features = response.getJSONArray("features");
                            for (int i = 0; i < Constants.LIMIT; i++) {

                                JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
                                JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");

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

//                                earthQuake.setUpdated(properties.getLong("updated"));
//                                earthQuake.setTz(properties.getInt("tz"));
                                earthQuake.setUrl(properties.getString("url"));
                                earthQuake.setDetail(properties.getString("detail"));
//                                earthQuake.setFelt(properties.getInt("felt"));
//                                earthQuake.setCdi(properties.getDouble("cdi"));
//                                earthQuake.setMmi(properties.getDouble("mmi"));
//                                earthQuake.setAlert(properties.getString("alert"));
//                                earthQuake.setStatus(properties.getString("status"));
//                                earthQuake.setTsunami(properties.getInt("tsunami"));
//                                earthQuake.setSig(properties.getInt("sig"));
//                                earthQuake.setNet(properties.getString("net"));
//                                earthQuake.setCode(properties.getString("code"));
//                                earthQuake.setIds(properties.getString("ids"));
//                                earthQuake.setSources(properties.getString("sources"));
//                                earthQuake.setTypes(properties.getString("types"));
//                                earthQuake.setNst(properties.getInt("nst"));
//                                earthQuake.setDmin(properties.getDouble("dmin"));
//                                earthQuake.setRms(properties.getDouble("rms"));
//                                earthQuake.setGap(properties.getDouble("gap"));
                                earthQuake.setMagType(properties.getString("magType"));
                                earthQuake.setType(properties.getString("type"));
                                earthQuake.setLongitude(longitude);
                                earthQuake.setLatitude(latitude);

                                //add to the map
                                MarkerOptions markerOptions = new MarkerOptions();

                                markerOptions.icon(iconColors[Constants.randomInt(iconColors.length, 0)]);

                                markerOptions.title(earthQuake.getPlace());
                                markerOptions.position( new LatLng(earthQuake.getLatitude(), earthQuake.getLongitude()));
                                markerOptions.snippet("Magnitude: " + earthQuake.getMag() + "\n" +
                                                      "Date: " + formattedDate);

                                //add circle to markers that have mag > X.
                                if (earthQuake.getMag() >= 2.0) {
                                    CircleOptions circleOptions = new CircleOptions();
                                    circleOptions.center( new LatLng( earthQuake.getLatitude(), earthQuake.getLongitude()));
                                    circleOptions.radius( 30000 );
                                    circleOptions.strokeWidth( 3.6f );
                                    circleOptions.fillColor( Color.RED);
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                                    mMap.addCircle(circleOptions);
                                }

                                Marker marker = mMap.addMarker(markerOptions);
                                marker.setTag(earthQuake.getDetail());
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(earthQuake.getLatitude(), earthQuake.getLongitude()), 6));

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
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        //Toast.makeText( getApplicationContext(), marker.getTitle().toString(), Toast.LENGTH_LONG).show();
        
        getQuakeDetails(marker.getTag().toString());

    }

    private void getQuakeDetails(String url) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String detailsUrl = "";

                try {
                    JSONObject properties = response.getJSONObject("properties");
                    JSONObject products = properties.getJSONObject("products");
                    JSONArray geoserve = products.getJSONArray("geoserve");

                    for ( int i = 0; i < geoserve.length(); i++){
                        JSONObject geoserveObj = geoserve.getJSONObject( i );

                        JSONObject contentObj = geoserveObj.getJSONObject("contents" );
                        JSONObject geoserveJsonObj = contentObj.getJSONObject("geoserve.json");

                        detailsUrl = geoserveJsonObj.getString( "url" );
                    }
                    //Log.d( "details URL ", detailsUrl);
                    getMoreDetails(detailsUrl);

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

    public void getMoreDetails(String url){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET,
                url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                dialogBuilder = new AlertDialog.Builder( MapsActivity.this);
                View view = getLayoutInflater().inflate(R.layout.popup, null);

                Button dismissTopButton = view.findViewById( R.id.dismissPopTop );
                TextView popList = view.findViewById(R.id.popList);
                WebView webView = view.findViewById( R.id.htmlWebview );
                Button dissmissPop = view.findViewById( R.id.dismissPop );

                StringBuilder stringBuilder = new StringBuilder();


                try {

                    if (response.has("tectonicSummary") && response.getString( "tectonicSummary" ) != null){

                        JSONObject tectonicSummary = response.getJSONObject("tectonicSummary");

                        if (tectonicSummary.has("text") && tectonicSummary.getString("text") != null){

                            String webText = tectonicSummary.getString( "text" );
                            webView.loadDataWithBaseURL(null, webText, "text/html", "UTF-8", null);
                        }
                    }

                    JSONArray cities = response.getJSONArray( "cities" );
                    for (int i = 0; i < cities.length(); i++) {

                        JSONObject citiesDetails = cities.getJSONObject(i);
                        stringBuilder.append("City: " + citiesDetails.getString( "name" ) +
                                "\n" + "Distance: " + citiesDetails.getString( "distance") +
                                "\n" + "Population: " + citiesDetails.getString( "population"));
                        stringBuilder.append("\n\n");
                    }

                    popList.setText(stringBuilder);

                    dialogBuilder.setView(view);
                    alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    dismissTopButton.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    } );
                    dissmissPop.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    } );

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
