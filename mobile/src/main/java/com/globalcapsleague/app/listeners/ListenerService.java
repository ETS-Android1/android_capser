package com.globalcapsleague.app.listeners;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.globalcapsleague.app.activity.MainActivity;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class ListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
        startIntent.putExtra("gameSpecs",new String(messageEvent.getData()));
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startIntent.addCategory(new String(messageEvent.getData()));

        SharedPreferences sharedPreferences = getSharedPreferences("game",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("game",new String(messageEvent.getData())).commit();
        startActivity(startIntent);
    }


}
