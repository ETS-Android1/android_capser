package com.example.globalcapsleagueapp.activity;

import android.app.Activity;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.wear.activity.ConfirmationActivity;

import com.example.globalcapsleagueapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.wearable.intent.RemoteIntent;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class SaveGameActivity extends Activity {

    private String savingNodeId = null;
    private String gameSpecs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_game);
        Intent startingIntent =  getIntent();
        gameSpecs = startingIntent.getStringExtra("gameSpecs");

        findViewById(R.id.save_for_later_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.go_to_phone).setOnClickListener(v -> {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendMessage();
                }
            }).start();

            finish();
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
