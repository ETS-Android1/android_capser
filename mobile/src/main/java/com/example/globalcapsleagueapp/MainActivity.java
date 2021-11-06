package com.example.globalcapsleagueapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.globalcapsleagueapp.utils.Security;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences refreshPreferences = getSharedPreferences(getResources().getString(R.string.refresh_token), Context.MODE_PRIVATE);
        SharedPreferences accessPreferences = getSharedPreferences(getResources().getString(R.string.access_token), Context.MODE_PRIVATE);
        String refreshToken = refreshPreferences.getString(getResources().getString(R.string.refresh_token), null);



        if (refreshToken == null || Security.tokenExpired(refreshToken)) {
            refreshPreferences.edit().clear().apply();
            accessPreferences.edit().clear().apply();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else{
            Intent intent = new Intent(getApplicationContext(), AddGameActivity.class);
            startActivity(intent);
        }


    }
}