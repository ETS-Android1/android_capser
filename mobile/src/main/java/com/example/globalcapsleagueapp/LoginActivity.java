package com.example.globalcapsleagueapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends GclBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String serverUrl =  getResources().getString(R.string.server_url) + "/api/login";

        Button loginButton = findViewById(R.id.login_button);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        getSupportActionBar().hide();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        loginButton.setOnClickListener(l ->{
            Log.i("Test","Loggin " + username.getText().toString() + password.getText());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Response",response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        SharedPreferences accessTokenPreferences = getSharedPreferences(getResources().getString(R.string.access_token),Context.MODE_PRIVATE);
                        SharedPreferences refreshTokenPreferences = getSharedPreferences(getResources().getString(R.string.refresh_token),Context.MODE_PRIVATE);

                        accessTokenPreferences.edit().putString(getResources().getString(R.string.access_token),jsonObject.getString("authToken")).commit();
                        refreshTokenPreferences.edit().putString(getResources().getString(R.string.refresh_token),jsonObject.getString("refreshToken")).commit();

                        Intent intent = new Intent(getApplicationContext(),AddGameActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error",":(");
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("username",username.getText().toString());
                    params.put("password",password.getText().toString());
                    return params;
                }
            };

            requestQueue.add(stringRequest);

        });



    }
}
