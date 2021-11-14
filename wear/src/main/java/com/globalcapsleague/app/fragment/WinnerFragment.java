package com.globalcapsleague.app.fragment;

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
import com.globalcapsleague.app.activity.SaveGameActivity;
import com.globalcapsleague.app.activity.game.GameActivity;

public class WinnerFragment extends Fragment {

    public TextView victoryText;
    public TextView consolationText;
    private boolean player1Won;
    private Handler handler = new Handler();
    private Runnable resetRunnable;
    private GameActivity parent;

    public WinnerFragment() {
        super(R.layout.fragment_game_winner);

    }

    public void setParentActivity(GameActivity parent){
        this.parent = parent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        victoryText = view.findViewById(R.id.victory_text);
        consolationText = view.findViewById(R.id.consolation_text);
        victoryText.setText(player1Won ? "Victory" : "Loss");
        consolationText.setText(player1Won ? "Congratulations" : "Better luck next time");
        victoryText.setTextColor(player1Won ? Color.GREEN : Color.RED);

        if (resetRunnable != null) {
            handler.removeCallbacks(resetRunnable);
        }
        resetRunnable = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getContext(), SaveGameActivity.class);

                i.putExtra("gameSpecs",parent.getGameJson());

                startActivity(i);
                parent.finish();
            }
        };


        handler.postDelayed(resetRunnable, 3000);

    }

    public void updateWinner(boolean player1Won) {
        this.player1Won = player1Won;
    }
}
