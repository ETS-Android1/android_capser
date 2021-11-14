package com.globalcapsleague.app.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.activity.game.GameActivity;
import com.globalcapsleague.app.components.SlideComponent;

public class MainFragment extends Fragment implements MainFragmentInterface {

    TextView player1ScoreDisplay;
    TextView player2ScoreDisplay;

    private int player1Score;
    private int player2Score;

    SlideComponent player1Button;
    SlideComponent player2Button;

    private GameActivity gameActivity;

    public MainFragment(){

        super(R.layout.main_game_fragment);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        player1ScoreDisplay = view.findViewById(R.id.player1_score);
        player2ScoreDisplay = view.findViewById(R.id.player2_score);
        player1ScoreDisplay.setText("0");
        player2ScoreDisplay.setText("0");

        player1Button = view.findViewById(R.id.player1_button);
        player2Button = view.findViewById(R.id.player2_button);

        player1ScoreDisplay.invalidate();

        GameActivity gameActivity = (GameActivity) getActivity();

        player1Button.setOnClickListener(v -> {
            gameActivity.player1Sink();
        });

        player2Button.setOnClickListener(v -> {
            gameActivity.player2Sink();
        });

        displayScores();
    }

    @Override
    public void updateScores(int player1, int player2){
        player1Score = player1;
        player2Score = player2;
    }

    private void displayScores(){
        player1ScoreDisplay.setText(String.valueOf(player1Score));
        player2ScoreDisplay.setText(String.valueOf(player2Score));
    }

    public void updateOffsetPlayer1(int offset){
        player1Button.setSmoothOffset(offset);
    }

    public void updateOffsetPlayer2(int offset){
        player2Button.setSmoothOffset(offset);
    }
}
