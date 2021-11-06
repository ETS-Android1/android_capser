package com.example.globalcapsleagueapp.activity.game;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globalcapsleagueapp.R;
import com.example.globalcapsleagueapp.components.SlideComponent;

public class RebuttalFragment extends Fragment implements RebuttalFragmentInterface {

    private TextView rebuttalStreakText;
    private TextView rebuttalText;

    private boolean player1Turn;
    private GameActivity gameActivity;

    SlideComponent rebuttedYesButton;
    SlideComponent rebuttedNoButton;

    public RebuttalFragment(){
        super(R.layout.fragment_game_rebuttals);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rebuttalStreakText = view.findViewById(R.id.rebuttal_streak);
        rebuttalText = view.findViewById(R.id.rebuttal_text);
        setRebuttingPlayer(player1Turn);


        rebuttedYesButton = view.findViewById(R.id.rebutted_yes);
        rebuttedNoButton = view.findViewById(R.id.rebutted_no);

        gameActivity = (GameActivity) getActivity();

        rebuttedYesButton.setOnClickListener(v -> {
            gameActivity.rebuttalYes();
        });

        rebuttedNoButton.setOnClickListener(v -> {
            gameActivity.rebuttalNo();
        });

    }

    @Override
    public void setRebuttalStreak(int streak) {
        rebuttalStreakText.setText(String.valueOf(streak));
    }

    @Override
    public void setRebuttingPlayer(boolean player1) {
        player1Turn = player1;
        if(rebuttalStreakText==null){
            return;
        }
        if(player1){
            rebuttalText.setText("Did You rebut?");
        } else {
            rebuttalText.setText("Did opponent rebut?");
        }
    }

    public void updateOffsetYes(int offset){
        rebuttedYesButton.setSmoothOffset(offset);
    }

    public void updateOffsetNo(int offset){
        rebuttedNoButton.setSmoothOffset(offset);
    }

}
