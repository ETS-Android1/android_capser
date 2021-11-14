package com.globalcapsleague.app.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.activity.AddGameActivity;
import com.globalcapsleague.app.activity.LiveGameActivity;
import com.globalcapsleague.app.models.Game;
import com.google.gson.Gson;

public class WinnerFragment extends Fragment {

    private TextView winnerText;
    private TextView winnerSubtext;
    private boolean player1Won;
    private LiveGameActivity liveGameActivity;

    public void setLiveGameActivity(LiveGameActivity liveGameActivity) {
        this.liveGameActivity = liveGameActivity;
    }

    public WinnerFragment(){
        super(R.layout.fragment_winner);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        winnerText = view.findViewById(R.id.winner_text);
        winnerSubtext = view.findViewById(R.id.winner_subtext);
        if(player1Won){
            winnerText.setTextColor(Color.GREEN);
            winnerText.setText("Victory");
            winnerSubtext.setText("Congratulations!");
        } else {
            winnerText.setTextColor(Color.RED);
            winnerText.setText("Loss");
            winnerSubtext.setText("Better luck next time");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Game game = new Game();
                game.setPlayerPoints(liveGameActivity.player1Points);
                game.setOpponentPoints(liveGameActivity.player2Points);
                game.setPlayerSinks(liveGameActivity.player1Sinks);
                game.setOpponentSinks(liveGameActivity.player2Sinks);


                Intent intent = new Intent(getContext(), AddGameActivity.class);
                intent.putExtra("game",new Gson().toJson(game));
                startActivity(intent);
                liveGameActivity.finish();
            }
        },3000);
    }

    public void setWinner(boolean player1){
        player1Won=player1;
    }
}
