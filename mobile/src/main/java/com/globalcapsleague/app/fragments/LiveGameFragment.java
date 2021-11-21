package com.globalcapsleague.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.activity.MainActivity;
import com.globalcapsleague.app.fragments.game.RebuttalFragment;
import com.globalcapsleague.app.fragments.game.GameFragment;
import com.globalcapsleague.app.fragments.game.WinnerFragment;
import com.globalcapsleague.app.fragments.tutorial.TutorialMainFragment;

public class LiveGameFragment extends Fragment {


    public int player1Points = 0;
    public int player2Points = 0;
    public int player1Rebuttals = 0;
    public int player2Rebuttals = 0;
    public int player1Sinks = 0;
    public int player2Sinks = 0;
    public boolean player1Turn = false;

    private MainActivity mainActivity;


    public LiveGameFragment() {
        super(R.layout.child_fragment_holder);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("gameTutorial", Context.MODE_PRIVATE);
        boolean tutorialPassed = sharedPreferences.getBoolean("gameTutorial", false);
        if (!tutorialPassed) {
            mainActivity.setFragment(TutorialMainFragment.class);
            return;
        }

        getChildFragmentManager().beginTransaction().setReorderingAllowed(true)
                .add(R.id.child_fragment_container, GameFragment.class, null).commit();

    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {

        if (fragment instanceof GameFragment) {
            ((GameFragment) fragment).setMainActivity(mainActivity);
            ((GameFragment) fragment).setLiveGameFragment(this);
        } else if (fragment instanceof RebuttalFragment) {
            ((RebuttalFragment) fragment).setLiveGameFragment(this);
            ((RebuttalFragment) fragment).setLiveGameFragment(this);
        } else if (fragment instanceof WinnerFragment) {
            ((WinnerFragment) fragment).setWinner(player1Points == 11);
            ((WinnerFragment) fragment).setMainActivity(mainActivity);
            ((WinnerFragment) fragment).setLiveGameFragment(this);
        }

    }


    public void displayRebuttalsScreen() {
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.child_fragment_container, RebuttalFragment.class, null)
                .commit();
    }

    public void hideRebuttalsScreen() {
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true)
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.child_fragment_container, GameFragment.class, null)
                .commit();
    }

    public void setFragment(Class<? extends Fragment> fragment) {
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true)
                .replace(R.id.child_fragment_container, fragment, null).commit();
    }

    public void setFragment(Class<? extends Fragment> fragment, int animationIn, int animationOut) {
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true)
                .setCustomAnimations(animationIn, animationOut)
                .replace(R.id.child_fragment_container, fragment, null).commit();
    }

}
