package com.globalcapsleague.app.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.fragments.GameFragment;
import com.globalcapsleague.app.fragments.RebuttalFragment;
import com.globalcapsleague.app.fragments.WinnerFragment;

public class LiveGameActivity extends GclBarActivity{


    public int player1Points =0;
    public int player2Points=0;
    public int player1Rebuttals=0;
    public int player2Rebuttals=0;
    public int player1Sinks=0;
    public int player2Sinks=0;
    public boolean player1Turn = false;

    private boolean rebuttalsScreen = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_holder);
        setupActionBar();
        navigationView.setCheckedItem(R.id.nav_play_game);

        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .add(R.id.fragment_container, GameFragment.class, null).commit();

        ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) findViewById(R.id.fragment_container).getLayoutParams();
        newLayoutParams.topMargin = (int) (25 * getApplicationContext().getResources().getDisplayMetrics().density) + 50;
        findViewById(R.id.fragment_container).setLayoutParams(newLayoutParams);
    }


    @Override
    public void onAttachFragment(Fragment fragment) {

        if (fragment instanceof GameFragment) {
            ((GameFragment) fragment).setContext(getApplicationContext());
            ((GameFragment) fragment).setMainActivity(this);
        } else if (fragment instanceof RebuttalFragment) {
            ((RebuttalFragment) fragment).setContext(getApplicationContext());
            ((RebuttalFragment) fragment).setMainActivity(this);
        } else if(fragment instanceof WinnerFragment){
            ((WinnerFragment) fragment).setWinner(player1Points==11);
            ((WinnerFragment) fragment).setLiveGameActivity(this);
        }

    }


    public void displayRebuttalsScreen() {
        rebuttalsScreen = true;
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_container, RebuttalFragment.class, null)
                .commit();
    }

    public void hideRebuttalsScreen(){
        rebuttalsScreen = false;
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_container, GameFragment.class, null)
                .commit();
    }
}
