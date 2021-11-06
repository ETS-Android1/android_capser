package com.example.globalcapsleagueapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class GclBarActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        actionBar.setBackgroundDrawable(colorDrawable);

        SpannableString spannableString = new SpannableString("GCL");
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "BankGothicBold.ttf");
        spannableString.setSpan(new TypefaceSpan(typeface),0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        actionBar.setTitle(spannableString);
    }
}
