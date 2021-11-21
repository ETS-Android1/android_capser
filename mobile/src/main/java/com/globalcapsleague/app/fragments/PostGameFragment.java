package com.globalcapsleague.app.fragments;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.activity.MainActivity;
import com.globalcapsleague.app.adapters.SelectOpponentAdapter;
import com.globalcapsleague.app.data.Fetch;
import com.globalcapsleague.app.enums.GameMode;
import com.globalcapsleague.app.models.Game;
import com.globalcapsleague.app.models.GameDto;
import com.globalcapsleague.app.models.OpponentObject;
import com.globalcapsleague.app.models.PlayerStatsDto;
import com.globalcapsleague.app.utils.RefreshHandler;
import com.globalcapsleague.app.utils.Security;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostGameFragment extends Fragment {


    private final List<OpponentObject> currentlyFetchedPlayers = new ArrayList<>();
    private String accessToken;
    private String username;

    private String opponentId = null;

    private Spinner playerPoints;
    private Spinner opponentPoints;

    private Spinner playerSinks;
    private Spinner opponentSinks;

    private AutoCompleteTextView autoCompleteTextView;
    private SelectOpponentAdapter opponentsAdapter;

    private CheckBox opponentSelectedCorrectly;
    private TextView opponentIdHelper;

    private MainActivity mainActivity;

    private Fetch fetch;

    public PostGameFragment() {
        super(R.layout.activity_add_game);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        fetch = new Fetch(mainActivity);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initializeFields(view);
        extractGameFromIntent();

        populateWithWatchDataIfAvailable();
        setUpAutocompleteView();
    }

    @Override
    public void onDestroyView() {
        resetFields();
        super.onDestroyView();
    }

    public void resetFields() {
        playerPoints.setSelection(0);
        playerSinks.setSelection(0);
        opponentPoints.setSelection(0);
        opponentSinks.setSelection(0);
        autoCompleteTextView.setText("");
        opponentId = null;
    }


    private void populateWithWatchDataIfAvailable() {
        if (mainActivity.getGameFromWatch() != null) {
            wakeUpScreen();
            populateWithWatchData();
        }
    }


    private void extractGameFromIntent() {
        Intent intent = mainActivity.getIntent();
        String gameString = intent.getStringExtra("game");
        if (gameString != null) {
            Game game = new Gson().fromJson(gameString, Game.class);
            playerPoints.setSelection(game.getPlayerPoints());
            playerSinks.setSelection(game.getPlayerSinks());
            opponentPoints.setSelection(game.getOpponentPoints());
            opponentSinks.setSelection(game.getOpponentSinks());
        }
        mainActivity.setIntent(new Intent());

    }

    private void wakeUpScreen() {
        KeyguardManager keyguardManager = (KeyguardManager) mainActivity.getSystemService(Context.KEYGUARD_SERVICE);
        keyguardManager.requestDismissKeyguard(mainActivity, null);
        mainActivity.setTurnScreenOn(true);
    }

    private void populateWithWatchData() {

        playerPoints.setSelection(mainActivity.getGameFromWatch().getPlayerPoints());
        playerSinks.setSelection(mainActivity.getGameFromWatch().getPlayerSinks());
        opponentPoints.setSelection(mainActivity.getGameFromWatch().getOpponentPoints());
        opponentSinks.setSelection(mainActivity.getGameFromWatch().getOpponentSinks());

        Toast toast = Toast.makeText(mainActivity.getApplicationContext(), "Game data received from watch", Toast.LENGTH_SHORT);
        toast.show();

    }

    private void initializeFields(View view) {
        Button addGameButton = view.findViewById(R.id.add_game_button);

        Spinner dropdown = view.findViewById(R.id.game_type_spinner);
        playerPoints = view.findViewById(R.id.player_points_spinner);
        opponentPoints = view.findViewById(R.id.opponent_points_spinner);

        playerSinks = view.findViewById(R.id.player_sinks_spinner);
        opponentSinks = view.findViewById(R.id.opponent_sinks_spinner);

        opponentSelectedCorrectly = view.findViewById(R.id.opponent_selected);
        opponentIdHelper = view.findViewById(R.id.opponent_id_error_message);

        autoCompleteTextView = view.findViewById(R.id.opponent_select);

        String[] gameTypes = new String[]{"Sudden Death", "Overtime"};
        Integer[] points = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        Integer[] sinks = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
        List<OpponentObject> opponents = new ArrayList<>();


        ArrayAdapter<String> gameTypesAdapter = new ArrayAdapter<>(mainActivity, R.layout.dropdown_item, gameTypes);
        ArrayAdapter<Integer> pointsAdapter = new ArrayAdapter<>(mainActivity, R.layout.dropdown_item, points);
        ArrayAdapter<Integer> sinksAdapter = new ArrayAdapter<>(mainActivity, R.layout.dropdown_item, sinks);
        opponentsAdapter = new SelectOpponentAdapter(mainActivity, R.layout.dropdown_item, opponents);

        autoCompleteTextView.setAdapter(opponentsAdapter);


        dropdown.setAdapter(gameTypesAdapter);
        playerPoints.setAdapter(pointsAdapter);
        opponentPoints.setAdapter(pointsAdapter);

        playerSinks.setAdapter(sinksAdapter);
        opponentSinks.setAdapter(sinksAdapter);


        SharedPreferences accessPreferences = mainActivity.getSharedPreferences(mainActivity.getResources().getString(R.string.access_token), Context.MODE_PRIVATE);
        SharedPreferences usernamePreferences = mainActivity.getSharedPreferences("username", Context.MODE_PRIVATE);
        accessToken = accessPreferences.getString(mainActivity.getResources().getString(R.string.access_token), null);
        username = usernamePreferences.getString("username", null);
        String userId = Security.getUserId(accessToken);


        addGameButton.setOnClickListener(l -> makeGameRequest(userId));

    }

    private void setUpAutocompleteView() {
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            OpponentObject selectedOpponent = opponentsAdapter.getItem(position);
            opponentSelectedCorrectly.setEnabled(true);
            opponentSelectedCorrectly.setChecked(true);
            opponentId = selectedOpponent.getId();
            opponentIdHelper.setText("");

        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (currentlyFetchedPlayers.stream().noneMatch(o -> o.getName().toLowerCase().equals(s.toString()))) {
                    opponentId = null;
                    opponentSelectedCorrectly.setChecked(false);
                    opponentSelectedCorrectly.setEnabled(false);
                    opponentIdHelper.setText("Select opponent name from the dropdown");
                }

                fetch.fetchUsernames(s.toString(), response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray names = jsonObject.getJSONArray("content");

                        List<OpponentObject> newNames = new ArrayList<>();
                        for (int i = 0; i < names.length(); i++) {
                            JSONObject name = names.getJSONObject(i);
                            if (name.getString("username").equals(username)) {
                                continue;
                            }
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


                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void makeGameRequest(String userId) {
        if (opponentId == null) {
            Toast toast = Toast.makeText(mainActivity.getApplicationContext(), "You have to select an opponent", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

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

        fetch.postGame(gameDto, response -> {

            CharSequence text = "Added game";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(mainActivity.getApplicationContext(), text, duration);
            toast.show();

            Intent intent = new Intent(mainActivity.getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }, error -> {


            try {
                CharSequence text = new JSONObject(new String(error.networkResponse.data)).getString("error");
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(mainActivity.getApplicationContext(), text, duration);
                toast.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }


}
