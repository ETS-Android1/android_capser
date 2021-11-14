package com.globalcapsleague.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.globalcapsleague.app.activity.LiveGameActivity;
import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.views.RotatableView;

public class GameFragment extends Fragment {

    private RotatableView playerCard;
    private RotatableView opponentCard;
    private ConstraintLayout playerCardDummy;
    private ConstraintLayout opponentCardDummy;
    private Context context;
    private LiveGameActivity mainActivity;

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

    public void setContext(Context context) {
        this.context = context;
    }

    public void setMainActivity(LiveGameActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setCardNames(view);
        updatePoints();
        setupTooltip(view);


        opponentCard.setOnClickListener(l -> {

            mainActivity.player1Turn = true;
            mainActivity.player2Sinks += 1;
            opponentSinks.setText(String.valueOf(mainActivity.player2Sinks));
            opponentSinksDummy.setText(String.valueOf(mainActivity.player2Sinks + 1));
            playerSinksDummy.setText(String.valueOf(mainActivity.player1Sinks));

            playerCard.animateReverse(() -> {
                playerSinksDummy.setText(String.valueOf(mainActivity.player1Sinks + 1));
                mainActivity.displayRebuttalsScreen();
            });
        });

        playerCard.setOnClickListener(l -> {
            mainActivity.player1Turn = false;

            mainActivity.player1Sinks += 1;
            playerSinks.setText(String.valueOf(mainActivity.player1Sinks));
            playerSinksDummy.setText(String.valueOf(mainActivity.player1Sinks + 1));
            opponentSinksDummy.setText(String.valueOf(mainActivity.player2Sinks));


            opponentCard.animateReverse(() -> {
                opponentSinksDummy.setText(String.valueOf(mainActivity.player2Sinks + 1));
                mainActivity.displayRebuttalsScreen();
            });

        });
    }


    private void setupTooltip(View view) {
        ConstraintLayout tooltip = view.findViewById(R.id.game_help_tooltip);
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("game_help_tooltip", Context.MODE_PRIVATE);
        boolean showTooltip = sharedPreferences.getBoolean("game_help_tooltip", true);
        if (showTooltip) {
            tooltip.setVisibility(View.VISIBLE);
        } else {
            tooltip.setVisibility(View.GONE);
        }

        tooltip.setOnClickListener(l -> {
            sharedPreferences.edit().putBoolean("game_help_tooltip", false).apply();
            tooltip.setVisibility(View.GONE);
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
        updateText(playerPoints, mainActivity.player1Points);
        updateText(playerPointsDummy, mainActivity.player1Points);
        updateText(opponentPoints, mainActivity.player2Points);
        updateText(opponentPointsDummy, mainActivity.player2Points);
        updateText(playerSinks, mainActivity.player1Sinks);
        updateText(playerSinksDummy, mainActivity.player1Sinks + 1);
        updateText(opponentSinks, mainActivity.player2Sinks);
        updateText(opponentSinksDummy, mainActivity.player2Sinks + 1);
        updateText(playerRebuttals, mainActivity.player1Rebuttals);
        updateText(playerRebuttalsDummy, mainActivity.player1Rebuttals);
        updateText(opponentRebuttals, mainActivity.player2Rebuttals);
        updateText(opponentRebuttalsDummy, mainActivity.player2Rebuttals);
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
