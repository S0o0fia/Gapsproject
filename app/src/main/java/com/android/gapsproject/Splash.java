package com.android.gapsproject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.android.gapsproject.R.id.container;
import static com.android.gapsproject.R.id.address;
import static com.android.gapsproject.R.id.splash;

public class Splash extends AppCompatActivity {

    public  static  int SPLASH_TIME_OUT = 4000;
    String json_url = "http://localhost/androidVolley.php";
    EditText username , password;
    private Context mContext;
    private Activity mActivity;
    Data data ;
    private  boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.killBackgroundProcesses("com.android.gapsproject");
            System.exit(0);
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
        setContentView(R.layout.activity_main);
        data = new Data();
        mContext = getApplicationContext();
        mActivity = Splash.this;
        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);

    }
    public void movetopage(View view) {
        if (view.getId() == R.id.loginbutton) {

            if ((username.getText().toString().equals("admin")) && (password.getText().toString().equals("admin"))) {
                Intent i = new Intent(Splash.this, MapsActivity.class);
                startActivity(i);
            } else {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, json_url
                                , null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    data.setLogin(response.getBoolean("login"));
                                    if (data.isLogin() == true) {
                                        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                                        startActivity(i);
                                    } else
                                        Toast.makeText(Splash.this, "username or password is wrong", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(Splash.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        });

                        requestQueue.add(jsonObjectRequest);
                    }
                });
            }
        }
    }
    public void gotocraeteacount(View view) {
        if (view.getId() == R.id.signup) {
            startActivity(new Intent(Splash.this , Register.class));
        }


    }

}

