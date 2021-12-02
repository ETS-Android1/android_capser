package com.globalcapsleague.app.data;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.activity.MainActivity;
import com.globalcapsleague.app.models.GameDto;
import com.globalcapsleague.app.models.GameFromApiDto;
import com.globalcapsleague.app.models.OpponentObject;
import com.globalcapsleague.app.utils.RefreshHandler;
import com.globalcapsleague.enums.GameType;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fetch {

    private final String searchPlayerUrl;
    private String dashboardGamesUrl;
    private String serverUrl;
    private final String addGameUrl;
    private final String fullUserUrl;
    private String accessToken;

    private RefreshHandler refreshHandler;
    private RequestQueue requestQueue;
    private StringRequestParse gameRequest;
    private StringRequestParse fullUserRequest;

    public Fetch(AppCompatActivity context) {
        this.requestQueue = Volley.newRequestQueue(context);
        searchPlayerUrl = context.getResources().getString(R.string.server_url) + "/api/users/search?pageSize=5&pageNumber=0&username=";
        addGameUrl = context.getResources().getString(R.string.server_url) + "/api/easy";
        fullUserUrl = context.getResources().getString(R.string.server_url) + "/api/users/${user}/full";
        dashboardGamesUrl = context.getResources().getString(R.string.server_url) + "/api/dashboard/games";
        serverUrl = context.getResources().getString(R.string.server_url);
        SharedPreferences accessPreferences = context.getSharedPreferences(context.getResources().getString(R.string.access_token), Context.MODE_PRIVATE);
        accessToken = accessPreferences.getString(context.getResources().getString(R.string.access_token), null);

        refreshHandler = new RefreshHandler(context.getApplicationContext());

    }

    public void fetchUsernames(String searchPhrase, FetchInterface fetchInterface) {
        StringRequestParse stringRequest = new StringRequestParse(Request.Method.GET, searchPlayerUrl + searchPhrase, response -> {
                fetchInterface.run(response);

        }, error -> {

        });
        requestQueue.add(stringRequest);
    }

    public void postGame(GameDto gameDto,FetchInterface success, FetchError errorHandler){
        gameRequest = new StringRequestParse(Request.Method.POST, addGameUrl, response -> {

            success.run(response);

        }, error -> {
            if (error.networkResponse.statusCode == 403 || error.networkResponse.statusCode == 401) {
                retryGameRequest();
            } else {
                errorHandler.run(error);
            }

        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

            @Override
            public byte[] getBody() {
                Gson gson = new Gson();
                return gson.toJson(gameDto).getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        requestQueue.add(gameRequest);
    }

    public void fetchFullUser(String userId,FetchInterface success, FetchError fetchError){
        fullUserRequest = new StringRequestParse(Request.Method.GET, fullUserUrl.replace("${user}",userId), response -> {

            success.run(response);

        }, error -> {
            if(error.networkResponse==null){
                return;
            }
            if (error.networkResponse.statusCode == 403 || error.networkResponse.statusCode == 401) {
                retryFetchUserRequest();
            } else {
                fetchError.run(error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }

        };

        requestQueue.add(fullUserRequest);
    }

    public void fetchDashboardGames(FetchInterface success){
        StringRequestParse stringRequest = new StringRequestParse(Request.Method.GET, dashboardGamesUrl, response -> {
            success.run(response);

        }, error -> {

        });
        requestQueue.add(stringRequest);
    }

    public void fetchGamesOfType(GameType gameType, FetchInterface success){
        StringRequestParse stringRequest = new StringRequestParse(Request.Method.GET, serverUrl+"/" + GameFromApiDto.getGameApiString(gameType), response -> {
            success.run(response);

        }, error -> {

        });
        requestQueue.add(stringRequest);
    }



    public static interface FetchInterface {
        void run(String response);
    }


    public static interface FetchError {
        void run(VolleyError response);
    }

    private void retryFetchUserRequest() {
        refreshHandler.refreshTokenAndDo((accessToken) -> {
            this.accessToken = accessToken;
            requestQueue.add(fullUserRequest);
        });

    }

    private void retryGameRequest() {
        refreshHandler.refreshTokenAndDo((accessToken) -> {
            this.accessToken = accessToken;
            requestQueue.add(gameRequest);
        });

    }
}
