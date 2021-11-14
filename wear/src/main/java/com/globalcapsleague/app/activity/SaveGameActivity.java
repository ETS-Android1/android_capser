package com.globalcapsleague.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.wear.activity.ConfirmationActivity;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.models.Game;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class SaveGameActivity extends Activity {

    private String savingNodeId = null;
    private String gameSpecs;

    private TextView player1Points;
    private TextView player2Points;
    private TextView player1Sinks;
    private TextView player2Sinks;
    private TextView player1Rebuttals;
    private TextView player2Rebuttals;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_game);
        Intent startingIntent =  getIntent();
        gameSpecs = startingIntent.getStringExtra("gameSpecs");
        Game game = new Gson().fromJson(gameSpecs,Game.class);
        ScrollView resultsContainer = findViewById(R.id.results_container);
        resultsContainer.requestFocus();

        player1Points = findViewById(R.id.player1_points);
        player2Points = findViewById(R.id.player2_points);
        player1Sinks = findViewById(R.id.player1_sinks);
        player2Sinks = findViewById(R.id.player2_sinks);
        player1Rebuttals = findViewById(R.id.player1_rebuttals);
        player2Rebuttals = findViewById(R.id.player2_rebuttals);

        player1Points.setText(String.valueOf(game.getPlayerPoints()));
        player2Points.setText(String.valueOf(game.getOpponentPoints()));
        player1Sinks.setText(String.valueOf(game.getPlayerSinks()));
        player2Sinks.setText(String.valueOf(game.getOpponentSinks()));
        player1Rebuttals.setText(String.valueOf(game.getPlayerRebuttals()));
        player2Rebuttals.setText(String.valueOf(game.getOpponentRebuttals()));

        resultsContainer.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_SCROLL &&
                        event.isFromSource(InputDeviceCompat.SOURCE_ROTARY_ENCODER)
                ) {
                    float delta = -event.getAxisValue(MotionEventCompat.AXIS_SCROLL) *
                            ViewConfigurationCompat.getScaledVerticalScrollFactor(
                                    ViewConfiguration.get(getApplicationContext()), getApplicationContext()
                            );

                    ((ScrollView)v).smoothScrollBy(0, Math.round(delta));

                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.save_for_later_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.save_on_phone_button).setOnClickListener(v -> {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendMessage();
                }
            }).start();

        });


    }

    @WorkerThread
    private void sendMessage(){

        try {
            CapabilityInfo capabilityInfo = Tasks.await(Wearable.getCapabilityClient(getApplicationContext()).getCapability(
                    "adding_games", CapabilityClient.FILTER_REACHABLE));
            savingNodeId = pickBestNodeId(capabilityInfo.getNodes());

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (savingNodeId != null) {

            Task<Integer> sendTask = Wearable.getMessageClient(getApplicationContext()).sendMessage(savingNodeId, "add_game", gameSpecs.getBytes(StandardCharsets.UTF_8));
            sendTask.addOnSuccessListener(new OnSuccessListener<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    Log.i("Succes", "suc");
                    Intent intent = new Intent(getApplicationContext(), GoToPhoneActivity.class);
                    intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                            ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
                    intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, "Go to phone!");
                    startActivity(intent);
                    finish();
                }
            });
            sendTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Faild", "faild");
                }
            });

        }
    }

    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }
}
