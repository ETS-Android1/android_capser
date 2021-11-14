package com.globalcapsleague.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.utils.Security;
import com.google.android.material.navigation.NavigationView;

public class GclBarActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences refreshPreferences = getSharedPreferences(getResources().getString(R.string.refresh_token), Context.MODE_PRIVATE);

        String refreshToken = refreshPreferences.getString(getResources().getString(R.string.refresh_token), null);

        if (refreshToken == null || Security.tokenExpired(refreshToken)) {
            logout();
        }
    }


    public void populateNameAndEmail(){
        SharedPreferences usernamePreferences = getSharedPreferences(getResources().getString(R.string.username), Context.MODE_PRIVATE);
        SharedPreferences emailPreferences = getSharedPreferences(getResources().getString(R.string.email), Context.MODE_PRIVATE);

        if(usernamePreferences!=null && emailPreferences!=null) {
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.header_name)).setText(usernamePreferences.getString(getResources().getString(R.string.username), ""));
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.header_email)).setText(emailPreferences.getString(getResources().getString(R.string.email), ""));
        }

    }

    public void setupActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.access_token, R.string.refresh_token);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        SpannableString spannableString = new SpannableString("GCL");
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "BankGothicBold.ttf");
        spannableString.setSpan(new TypefaceSpan(typeface), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(spannableString);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView = findViewById(R.id.navigation_view);

        populateNameAndEmail();

        navigationView.setNavigationItemSelectedListener(l -> {
            int id = l.getItemId();
            if (id == R.id.nav_logout) {
                logout();
            } else if(id == R.id.nav_play_game){
                Intent intent = new Intent(this,LiveGameActivity.class);
                startActivity(intent);
            } else if(id == R.id.nav_home){
                if(this instanceof LiveGameActivity){
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                } else{
                    // #TODO fragment
                }
            }
            return true;
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        SharedPreferences refreshPreferences = getSharedPreferences(getResources().getString(R.string.refresh_token), Context.MODE_PRIVATE);
        SharedPreferences accessPreferences = getSharedPreferences(getResources().getString(R.string.access_token), Context.MODE_PRIVATE);
        refreshPreferences.edit().clear().apply();
        accessPreferences.edit().clear().apply();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout==null){
            super.onBackPressed();
            return;
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setFragment(Class<? extends Fragment> fragment){
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragment_container,fragment,null).commit();
    }

    public void setFragment(Class<? extends Fragment> fragment, int animationIn, int animationOut){
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .setCustomAnimations(animationIn,animationOut)
                .replace(R.id.fragment_container,fragment,null).commit();
    }
}
