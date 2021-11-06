package com.example.globalcapsleagueapp;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcapsleagueapp.enums.GameMode;
import com.example.globalcapsleagueapp.models.Game;
import com.example.globalcapsleagueapp.models.GameDto;
import com.example.globalcapsleagueapp.models.OpponentObject;
import com.example.globalcapsleagueapp.adapters.SelectOpponentAdapter;
import com.example.globalcapsleagueapp.models.PlayerStatsDto;
import com.example.globalcapsleagueapp.utils.RefreshHandler;
import com.example.globalcapsleagueapp.utils.Security;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGameActivity extends GclBarActivity {

    private String opponentId = null;
    private RefreshHandler refreshHandler;
    private StringRequest gameRequest;
    private RequestQueue requestQueue;
    private String accessToken;
    private final List<OpponentObject> currentlyFetchedPlayers = new ArrayList<>();
    private Game gameFromMobile;

    Spinner playerPoints;
    Spinner opponentPoints;

    Spinner playerSinks;
    Spinner opponentSinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("game",MODE_PRIVATE);

        setContentView(R.layout.activity_add_game);
        Intent intent = getIntent();
        String gameSpecs = sharedPreferences.getString("game",null);
        if (gameSpecs != null) {
            gameFromMobile = new Gson().fromJson(gameSpecs, Game.class);
            sharedPreferences.edit().putString("game",null).apply();
        }
        initialize();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        populateWithMobileData();
        setIntent(intent);
    }

    private void populateWithMobileData() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguardManager.requestDismissKeyguard(this,null);
        setTurnScreenOn(true);
        playerPoints.setSelection(gameFromMobile.getPlayerPoints());
        playerSinks.setSelection(gameFromMobile.getPlayerSinks());
        opponentPoints.setSelection(gameFromMobile.getOpponentPoints());
        opponentSinks.setSelection(gameFromMobile.getOpponentSinks());

        Toast toast = Toast.makeText(getApplicationContext(), "Game data received from watch", Toast.LENGTH_SHORT);
        toast.show();

    }

    private void initialize() {
        Button addGameButton = findViewById(R.id.add_game_button);

        Spinner dropdown = findViewById(R.id.game_type_spinner);
        playerPoints = findViewById(R.id.player_points_spinner);
        opponentPoints = findViewById(R.id.opponent_points_spinner);

        playerSinks = findViewById(R.id.player_sinks_spinner);
        opponentSinks = findViewById(R.id.opponent_sinks_spinner);



        CheckBox opponentSelectedCorrectly = findViewById(R.id.opponent_selected);
        TextView opponentIdHelper = findViewById(R.id.opponent_id_error_message);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.opponent_select);

        String[] gameTypes = new String[]{"Sudden Death", "Overtime"};
        Integer[] points = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        Integer[] sinks = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
        List<OpponentObject> opponents = new ArrayList<>();

        ArrayAdapter<String> gameTypesAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, gameTypes);
        ArrayAdapter<Integer> pointsAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, points);
        ArrayAdapter<Integer> sinksAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, sinks);
        SelectOpponentAdapter opponentsAdapter = new SelectOpponentAdapter(this, R.layout.dropdown_item, opponents);

        autoCompleteTextView.setAdapter(opponentsAdapter);

        requestQueue = Volley.newRequestQueue(this);
        String serverUrl = getResources().getString(R.string.server_url) + "/api/users/search?pageSize=5&pageNumber=0&username=";
        String gameUrl = getResources().getString(R.string.server_url) + "/api/easy";

        dropdown.setAdapter(gameTypesAdapter);
        playerPoints.setAdapter(pointsAdapter);
        opponentPoints.setAdapter(pointsAdapter);

        playerSinks.setAdapter(sinksAdapter);
        opponentSinks.setAdapter(sinksAdapter);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpponentObject selectedOpponent = opponentsAdapter.getItem(position);
                Log.i("Item", selectedOpponent.getId());
                opponentSelectedCorrectly.setEnabled(true);
                opponentSelectedCorrectly.setChecked(true);
                opponentId = selectedOpponent.getId();

            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("String", s.toString());
                if (currentlyFetchedPlayers.stream().noneMatch(o -> o.getName().toLowerCase().equals(s.toString())) && opponentId != null) {
                    opponentId = null;
                    opponentSelectedCorrectly.setChecked(false);
                    opponentSelectedCorrectly.setEnabled(false);
                    opponentIdHelper.setText("Select opponent name from the dropdown");
                }

                StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl + s.toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray names = jsonObject.getJSONArray("content");
                            Log.i("Res", response);

                            List<OpponentObject> newNames = new ArrayList<>();
                            for (int i = 0; i < names.length(); i++) {
                                JSONObject name = names.getJSONObject(i);
                                newNames.add(new OpponentObject(name.getString("username"), name.getString("id")));
                            }
                            opponentsAdapter.clear();
                            opponentsAdapter.addAll(newNames);
                            opponentsAdapter.notifyDataSetChanged();

                            currentlyFetchedPlayers.clear();
                            currentlyFetchedPlayers.addAll(newNames);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(stringRequest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        SharedPreferences accessPreferences = getSharedPreferences(getResources().getString(R.string.access_token), Context.MODE_PRIVATE);
        accessToken = accessPreferences.getString(getResources().getString(R.string.access_token), null);

        String userId = Security.getUserId(accessToken);

        refreshHandler = new RefreshHandler(getApplicationContext());

        addGameButton.setOnClickListener(l -> {

            GameDto gameDto = new GameDto();
            gameDto.setGameMode(GameMode.SUDDEN_DEATH);
            PlayerStatsDto player1Stats = new PlayerStatsDto();
            player1Stats.setPlayerId(userId);
            player1Stats.setScore(Integer.parseInt(playerPoints.getSelectedItem().toString()));
            player1Stats.setSinks(Integer.parseInt(playerSinks.getSelectedItem().toString()));

            PlayerStatsDto player2Stats = new PlayerStatsDto();
            player2Stats.setPlayerId(opponentId);
            player2Stats.setScore(Integer.parseInt(opponentPoints.getSelectedItem().toString()));
            player2Stats.setSinks(Integer.parseInt(opponentSinks.getSelectedItem().toString()));

            gameDto.setPlayer1Stats(player1Stats);
            gameDto.setPlayer2Stats(player2Stats);

            gameDto.setGameEventList(new ArrayList<>());

            gameRequest = new StringRequest(Request.Method.POST, gameUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    CharSequence text = "Added game";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse.statusCode == 403 || error.networkResponse.statusCode == 401) {
                        retryGameRequest();
                        return;
                    }

                    CharSequence text = null;
                    try {
                        text = new JSONObject(new String(error.networkResponse.data)).getString("error");
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                        toast.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Bearer " + accessToken);
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    Gson gson = new Gson();
                    return gson.toJson(gameDto).getBytes(StandardCharsets.UTF_8);
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            requestQueue.add(gameRequest);
        });
        if (gameFromMobile != null) {
            populateWithMobileData();
        }

    }

    private void retryGameRequest() {
        refreshHandler.refreshTokenAndDo((accessToken) -> {
            this.accessToken = accessToken;
            requestQueue.add(gameRequest);
        });

    }
}

