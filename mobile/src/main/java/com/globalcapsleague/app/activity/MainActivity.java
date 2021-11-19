package com.globalcapsleague.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.fragments.HomeFragment;
import com.globalcapsleague.app.fragments.PostGameFragment;
import com.globalcapsleague.app.fragments.tutorial.TutorialMainFragment;
import com.globalcapsleague.app.models.Game;
import com.google.gson.Gson;

public class MainActivity extends GclBarActivity {

    private Game gameFromWatch;

    public Game getGameFromWatch() {
        return gameFromWatch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_holder);
        setupActionBar();


        extractGameFromPreferences();

        Intent intent = getIntent();
        if(intent.getStringExtra("fragment")!=null){
            if(intent.getStringExtra("fragment").equals("postGame")){
                navigationView.setCheckedItem(R.id.nav_add_game);
                setFragment(PostGameFragment.class);
            }
        } else {
            navigate(R.id.nav_home);
        }
    }

    private void extractGameFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("game", MODE_PRIVATE);
        String gameSpecs = sharedPreferences.getString("game", null);
        if (gameSpecs != null) {
            gameFromWatch = new Gson().fromJson(gameSpecs, Game.class);
            sharedPreferences.edit().putString("game", null).apply();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {

        if (fragment instanceof PostGameFragment) {
            ((PostGameFragment) fragment).setMainActivity(this);
        }else  if(fragment instanceof LiveGameFragment){
            ((LiveGameFragment) fragment).setMainActivity(this);
        } else if(fragment instanceof TutorialMainFragment){
            ((TutorialMainFragment) fragment).setMainActivity(this);
        }

    }





}