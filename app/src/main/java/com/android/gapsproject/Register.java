package com.android.gapsproject;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText fname, lname, username, password;
    String json_url = "http://localhost/androidVolley.php";
    private Context mContext;
    private Activity mActivity;
    Data data;
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
        if( getIntent().getBooleanExtra("Exit me", false)){
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.killBackgroundProcesses("com.android.gapsproject");
            System.exit(0);
        }
        setContentView(R.layout.activity_register);
        mContext = getApplicationContext();
        mActivity = Register.this;
        fname = (EditText) findViewById(R.id.fname1);
        lname = (EditText) findViewById(R.id.lname1);
        username = (EditText) findViewById(R.id.usern1);
        password = (EditText) findViewById(R.id.pass1);
        data = new Data();
    }


    public void Register(View view) {
        data.setFname(fname.getText().toString());
        data.setLname(lname.getText().toString());
        data.setUsername(username.getText().toString());
        data.setPassword(password.getText().toString());

        if (view.getId() == R.id.signup) {

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url
                            , null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("Response", response.toString());

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(Register.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }) {

                        @Override
                        protected Map getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("fname", data.getFname());
                            params.put("lname", data.getLname());
                            params.put("usename", data.getUsername());
                            params.put("password", data.getPassword());
                            return params;
                        }

                    };
                    requestQueue.add(jsonObjectRequest);
                }
            });
        }

        Intent i = new Intent(Register.this, MapsActivity.class);
        startActivity(i);
    }
}


