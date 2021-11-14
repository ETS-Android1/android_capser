package com.globalcapsleague.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcapsleagueapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RefreshHandler {

    private Context context;
    private String refreshToken;
    private SharedPreferences accessPreferences;

    public RefreshHandler(Context context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.refresh_token),Context.MODE_PRIVATE);
        accessPreferences = context.getSharedPreferences(context.getResources().getString(R.string.access_token),Context.MODE_PRIVATE);
        refreshToken = sharedPreferences.getString(context.getResources().getString(R.string.refresh_token),null);
    }

    private RequestQueue requestQueue;

    public void refreshTokenAndDo(Test test) {
        JSONObject params = new JSONObject();
        try {
            params.put("refreshToken", refreshToken);

        } catch (JSONException ignored) {
            // never thrown in this case
        }

        StringRequest refreshTokenRequest = new StringRequest(Request.Method.POST, context.getString(R.string.server_url) + "/api/refresh",  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String accessToken = jsonObject.getString("authToken");
                    accessPreferences.edit().putString(context.getResources().getString(R.string.access_token),accessToken).apply();
                    test.makeIt(accessToken);
                } catch (JSONException e) {
                    // this will never happen but if so, show error to user.
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // show error to user. refresh failed.
                Log.e("Error on token refresh", new String(error.networkResponse.data));

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("refreshToken",refreshToken);
                return params;
            }
        };
        requestQueue.add(refreshTokenRequest);

    }



}
