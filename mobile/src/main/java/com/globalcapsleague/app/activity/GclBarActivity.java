package com.globalcapsleague.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
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
import com.globalcapsleague.app.data.Fetch;
import com.globalcapsleague.app.data.FetchImage;
import com.globalcapsleague.app.fragments.HomeFragment;
import com.globalcapsleague.app.fragments.LiveGameFragment;
import com.globalcapsleague.app.fragments.PostGameFragment;
import com.globalcapsleague.app.models.ProfileDto;
import com.globalcapsleague.app.utils.Security;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GclBarActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Fetch fetch;


    @Override
    public void onBackPressed() {
        if (drawerLayout == null) {
            super.onBackPressed();
            return;
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetch = new Fetch(this);

        fetchProfile();

        SharedPreferences refreshPreferences = getSharedPreferences(getResources().getString(R.string.refresh_token), Context.MODE_PRIVATE);

        String refreshToken = refreshPreferences.getString(getResources().getString(R.string.refresh_token), null);

        if (refreshToken == null || Security.tokenExpired(refreshToken)) {
            logout();
        }
    }


    private void fetchProfile(){
        SharedPreferences usernamePreferences = getSharedPreferences("userId", Context.MODE_PRIVATE);
        String userId = usernamePreferences.getString("userId",null);

        if(userId ==null){
            return;
        }
        fetch.fetchFullUser(userId,response ->{
            SharedPreferences profilePreferences = getSharedPreferences("profile",Context.MODE_PRIVATE);
            profilePreferences.edit().putString("profile",response).apply();
            ProfileDto profileDto = new Gson().fromJson(response,ProfileDto.class);

            new Thread(new FetchImage(profileDto.getAvatarHash(),result -> {
                runOnUiThread(() -> ((ShapeableImageView) navigationView.getHeaderView(0).findViewById(R.id.header_avatar)).setImageBitmap(result));
            })).start();

        }, error ->{

        });
    }


    public void populateNameAndEmail() {
        SharedPreferences usernamePreferences = getSharedPreferences(getResources().getString(R.string.username), Context.MODE_PRIVATE);
        SharedPreferences emailPreferences = getSharedPreferences(getResources().getString(R.string.email), Context.MODE_PRIVATE);

        if (usernamePreferences != null && emailPreferences != null) {
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
            navigate(id);

            return true;
        });
    }


    public void navigate(int id){
        if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_play_game) {
            setFragment(LiveGameFragment.class);
        } else if (id == R.id.nav_add_game) {
            setFragment(PostGameFragment.class);
        } else if (id == R.id.nav_home) {
            setFragment(HomeFragment.class);
        }
        navigationView.setCheckedItem(id);

        drawerLayout.closeDrawer(GravityCompat.START);
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


    public void setFragment(Class<? extends Fragment> fragment) {
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragment_container, fragment, null).commit();
    }

    public void setFragment(Class<? extends Fragment> fragment, int animationIn, int animationOut) {
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .setCustomAnimations(animationIn, animationOut)
                .replace(R.id.fragment_container, fragment, null).commit();
    }
}
