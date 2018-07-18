package com.android.gapsproject;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Address_Parking extends AppCompatActivity {


    Data data;
    TextView addr ;
    String address ;
    Context mContext;
    String json_url = "";
    String urlimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address__parking);
        //Initialize Image View
        ImageView imageview = (ImageView) findViewById(R.id.place);
        //loading image from below url into imageView
        Picasso.with(this)
                .load("")
                .into(imageview);
        getaddress();


    }


    void getaddress ()
    {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    address = response.getString("address");
                    data.setAddress(address);
                    addr.setText(address);
                    urlimage = response.getString("urlimage");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Address_Parking.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

}
