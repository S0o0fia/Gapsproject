package com.android.gapsproject;
import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.IntegerRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.location.places.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import  com.google.android.gms.location.places.*;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity  implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private Marker mbrisane , cargap ;
    private GoogleMap mMap;
    LocationManager locationmanger;
    double lat , lang ;
    final  int REQUEST_LOCATION = 1 ;
    String json_url = "http://localhost/androidVolley.php";
    double gaplat =25.7188 , gaplang =32.6573 ;
    String address ;
    private Context mContext;
    private Activity mActivity;
    Data data ;
    int count ;
    private  boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(this, Splash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Exit me", true);
            startActivity(intent);
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.killBackgroundProcesses("com.android.gapsproject");
            System.exit(0);
            finish();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_navigation);
        if( getIntent().getBooleanExtra("Exit me", false)){
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.killBackgroundProcesses("com.android.gapsproject");
            System.exit(0);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        data = new Data();
        address = "this is your gap";
        data.setAddress(address);
        count = 0;
    }

    //function to get the latitiude and langtitude of the Gap
    public void getlatlang ()
    {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                       gaplat = response.getDouble("lat");
                       gaplang = response.getDouble("lang");
                       address = response.getString("address");
                       data.setAddress(address);
                       data.setLat(gaplat);
                       data.setLang(gaplang);

                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        locationmanger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationmanger.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationmanger.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
            mMap = googleMap;
            MarkerOptions current  = new MarkerOptions();
            mbrisane = googleMap.addMarker(current
                    .position(new LatLng(lat , lang))
                    .title("This is is your loaction"));
            mbrisane.setTag(count++);
            LatLng curpos = new LatLng(lat , lang);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(curpos));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat , lang))      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }

        if((gaplat != 0 )&& (gaplang != 0))
        {
            MarkerOptions car = new MarkerOptions();
            cargap = googleMap.addMarker(car
                    .position(new LatLng(gaplat , gaplang))
                    .title(address));
            cargap.setTag(count++);
            mMap.setOnMarkerClickListener(this);

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if(marker.getTag()!= "0" )
        startActivity(new Intent(MapsActivity.this , Address_Parking.class));

        return false;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationmanger.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationmanger.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationmanger.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lat = latti;
                lang = longi;


            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lat = latti;
                lang = longi;


            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lat = latti;
                lang = longi;

            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
