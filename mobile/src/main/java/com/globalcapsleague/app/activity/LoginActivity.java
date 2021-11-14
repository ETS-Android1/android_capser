package com.globalcapsleague.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.utils.Security;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String serverUrl =  getResources().getString(R.string.server_url) + "/api/login";

        Button loginButton = findViewById(R.id.login_button);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        TextView loginError = findViewById(R.id.login_error);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        loginButton.setOnClickListener(l ->{
            Log.i("Test","Login " + username.getText().toString() + password.getText());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Response",response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        SharedPreferences accessTokenPreferences = getSharedPreferences(getResources().getString(R.string.access_token),Context.MODE_PRIVATE);
                        SharedPreferences refreshTokenPreferences = getSharedPreferences(getResources().getString(R.string.refresh_token),Context.MODE_PRIVATE);
                        SharedPreferences emailPreferences = getSharedPreferences(getResources().getString(R.string.email),Context.MODE_PRIVATE);
                        SharedPreferences usernamePreferences = getSharedPreferences(getResources().getString(R.string.username),Context.MODE_PRIVATE);

                        usernamePreferences.edit().putString(getResources().getString(R.string.username),username.getText().toString()).apply();
                        emailPreferences.edit().putString(getResources().getString(R.string.email), Security.getEmail(jsonObject.getString("authToken"))).apply();
                        accessTokenPreferences.edit().putString(getResources().getString(R.string.access_token),jsonObject.getString("authToken")).commit();
                        refreshTokenPreferences.edit().putString(getResources().getString(R.string.refresh_token),jsonObject.getString("refreshToken")).commit();

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
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
                    loginError.setText("Invalid username of password");
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
