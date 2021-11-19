package com.globalcapsleague.app.fragments.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.globalcapsleague.app.activity.LiveGameFragment;
import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.activity.MainActivity;
import com.globalcapsleague.app.views.RotatableView;

public class GameFragment extends Fragment {

    private RotatableView playerCard;
    private RotatableView opponentCard;
    private ConstraintLayout playerCardDummy;
    private ConstraintLayout opponentCardDummy;
    private Context context;
    private MainActivity mainActivity;
    private LiveGameFragment liveGameFragment;

    TextView opponentPoints;
    TextView opponentPointsDummy;
    TextView opponentSinks;
    TextView opponentSinksDummy;
    TextView opponentRebuttals;
    TextView opponentRebuttalsDummy;

    TextView playerPoints;
    TextView playerPointsDummy;
    TextView playerSinks;
    TextView playerSinksDummy;
    TextView playerRebuttals;
    TextView playerRebuttalsDummy;

    public GameFragment() {
        super(R.layout.fragment_game_layout);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context=mainActivity.getApplicationContext();
    }

    public void setLiveGameFragment(LiveGameFragment liveGameFragment) {
        this.liveGameFragment = liveGameFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setCardNames(view);
        updatePoints();


        opponentCard.setOnClickListener(l -> {

            liveGameFragment.player1Turn = true;
            liveGameFragment.player2Sinks += 1;
            opponentSinks.setText(String.valueOf(liveGameFragment.player2Sinks));
            opponentSinksDummy.setText(String.valueOf(liveGameFragment.player2Sinks + 1));
            playerSinksDummy.setText(String.valueOf(liveGameFragment.player1Sinks));

            playerCard.animateReverse(() -> {
                playerSinksDummy.setText(String.valueOf(liveGameFragment.player1Sinks + 1));
                liveGameFragment.displayRebuttalsScreen();
            });
        });

        playerCard.setOnClickListener(l -> {
            liveGameFragment.player1Turn = false;

            liveGameFragment.player1Sinks += 1;
            playerSinks.setText(String.valueOf(liveGameFragment.player1Sinks));
            playerSinksDummy.setText(String.valueOf(liveGameFragment.player1Sinks + 1));
            opponentSinksDummy.setText(String.valueOf(liveGameFragment.player2Sinks));


            opponentCard.animateReverse(() -> {
                opponentSinksDummy.setText(String.valueOf(liveGameFragment.player2Sinks + 1));
                liveGameFragment.displayRebuttalsScreen();
            });

        });
    }



    private void initializeViews(View view) {
        opponentCard = view.findViewById(R.id.bottom_card);
        playerCard = view.findViewById(R.id.top_card);

        opponentCardDummy = view.findViewById(R.id.bottom_card_dummy);
        opponentCard.setCameraDistance(15000 * context.getResources().getDisplayMetrics().density);


        playerCardDummy = view.findViewById(R.id.top_card_dummy);
        playerCard.setCameraDistance(15000 * context.getResources().getDisplayMetrics().density);

        opponentPoints = opponentCard.findViewById(R.id.details_points);
        opponentPointsDummy = opponentCardDummy.findViewById(R.id.details_points);
        opponentSinks = opponentCard.findViewById(R.id.details_sinks);
        opponentSinksDummy = opponentCardDummy.findViewById(R.id.details_sinks);
        opponentRebuttals = opponentCard.findViewById(R.id.details_rebuttals);
        opponentRebuttalsDummy = opponentCardDummy.findViewById(R.id.details_rebuttals);

        playerPoints = playerCard.findViewById(R.id.details_points);
        playerPointsDummy = playerCardDummy.findViewById(R.id.details_points);
        playerSinks = playerCard.findViewById(R.id.details_sinks);
        playerSinksDummy = playerCardDummy.findViewById(R.id.details_sinks);
        playerRebuttals = playerCard.findViewById(R.id.details_rebuttals);
        playerRebuttalsDummy = playerCardDummy.findViewById(R.id.details_rebuttals);

    }

    public void updatePoints() {
        updateText(playerPoints, liveGameFragment.player1Points);
        updateText(playerPointsDummy, liveGameFragment.player1Points);
        updateText(opponentPoints, liveGameFragment.player2Points);
        updateText(opponentPointsDummy, liveGameFragment.player2Points);
        updateText(playerSinks, liveGameFragment.player1Sinks);
        updateText(playerSinksDummy, liveGameFragment.player1Sinks + 1);
        updateText(opponentSinks, liveGameFragment.player2Sinks);
        updateText(opponentSinksDummy, liveGameFragment.player2Sinks + 1);
        updateText(playerRebuttals, liveGameFragment.player1Rebuttals);
        updateText(playerRebuttalsDummy, liveGameFragment.player1Rebuttals);
        updateText(opponentRebuttals, liveGameFragment.player2Rebuttals);
        updateText(opponentRebuttalsDummy, liveGameFragment.player2Rebuttals);
    }

    private void updateText(TextView text, int value) {
        text.setText(String.valueOf(value));
    }

    private void setCardNames(View view) {
        ((TextView) playerCard.findViewById(R.id.details_title)).setText("You");
        ((TextView) view.findViewById(R.id.top_card_dummy).findViewById(R.id.details_title)).setText("You");
        ((TextView) opponentCard.findViewById(R.id.details_title)).setText("Opponent");
        ((TextView) view.findViewById(R.id.bottom_card_dummy).findViewById(R.id.details_title)).setText("Opponent");
    }
}
