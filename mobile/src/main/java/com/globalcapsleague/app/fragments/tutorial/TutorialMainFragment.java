package com.globalcapsleague.app.fragments.tutorial;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.activity.MainActivity;

public class TutorialMainFragment extends Fragment {

    private int step = 0;
    private MainActivity mainActivity;
    private TextView next;

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public TutorialMainFragment(){
        super(R.layout.game_tutorial_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.game_tutorial_fragment_container,TutorialChildFragment.class,null).commit();
        view.findViewById(R.id.step1).setActivated(true);

        next = view.findViewById(R.id.tutorial_next);
        next.setOnClickListener(l -> {
            nextStep(view);
        });
    }

    private void nextStep(View view){
        step+=1;
        if(step==3){
            mainActivity.navigate(R.id.nav_play_game);
            SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("gameTutorial", Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("gameTutorial",true).apply();
            return;
        }
        if(step==1){
            view.findViewById(R.id.step2).setActivated(true);
        } else if(step==2){
            next.setText("PLAY");
            view.findViewById(R.id.step3).setActivated(true);
        }

        getChildFragmentManager().beginTransaction().setReorderingAllowed(true)
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.game_tutorial_fragment_container,TutorialChildFragment.class,null).commit();

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        switch (step){
            case 0:
                ((TutorialChildFragment)fragment).setTextAndImage("You are about to enter the game screen." +
                                " If you manage to sink a cap flip the scoreboard down if your opponent does sink flip the scoreboard up.\n\n After that you will enter rebuttals screen.",
                        R.drawable.main_game);
                break;
            case 1:
                ((TutorialChildFragment)fragment).setTextAndImage("Person rebutting is indicated by the green outline. " +
                        "\nIf someone rebutted drag the green card if not drag the red." +
                        " Then colors switch. Follow the green with rebuttals. \n\nIf someone fails you go back to previous screen.",R.drawable.rebuttal);
                break;
            case 2:
                ((TutorialChildFragment)fragment).setTextAndImage("Good luck and have fun in your games!",R.drawable.glhf);
                break;
        }

    }

}
