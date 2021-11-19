package com.globalcapsleague.app.fragments.game;

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
import com.globalcapsleague.app.activity.LiveGameFragment;
import com.globalcapsleague.app.activity.MainActivity;
import com.globalcapsleague.app.models.Game;
import com.google.gson.Gson;

public class WinnerFragment extends Fragment {

    private TextView winnerText;
    private TextView winnerSubtext;
    private boolean player1Won;
    private MainActivity mainActivity;
    private LiveGameFragment liveGameFragment;

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setLiveGameFragment(LiveGameFragment liveGameFragment) {
        this.liveGameFragment = liveGameFragment;
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
                game.setPlayerPoints(liveGameFragment.player1Points);
                game.setOpponentPoints(liveGameFragment.player2Points);
                game.setPlayerSinks(liveGameFragment.player1Sinks);
                game.setOpponentSinks(liveGameFragment.player2Sinks);


                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("fragment","postGame");
                intent.putExtra("game",new Gson().toJson(game));
                mainActivity.setIntent(intent);
                mainActivity.navigate(R.id.nav_add_game);
            }
        },3000);
    }

    public void setWinner(boolean player1){
        player1Won=player1;
    }
}
