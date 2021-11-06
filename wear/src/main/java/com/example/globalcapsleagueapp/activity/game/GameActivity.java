package com.example.globalcapsleagueapp.activity.game;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.wear.ambient.AmbientModeSupport;
import androidx.wear.widget.BoxInsetLayout;

import com.example.globalcapsleagueapp.R;
import com.example.globalcapsleagueapp.components.CurvedText;
import com.example.globalcapsleagueapp.models.Game;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimerTask;

public class GameActivity extends FragmentActivity implements GameActivityInterface, AmbientModeSupport.AmbientCallbackProvider {

    int player1Score;
    int player2Score;
    int player1Sinks;
    int player2Sinks;
    int player1Rebuttals;
    int player2Rebuttals;


    private boolean player1Won;
    private boolean gameFinished = false;

    private boolean player1Turn = true;
    private boolean inRebuttalSeries = false;

    private int currentRebuttalStreak;

    private Fragment currentFragment;
    private Long lastRotary = new Date().getTime();


    float currentRotation = 0;

    CurvedText curvedText;

    private Handler handler;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LocalTime time = LocalTime.now();
            curvedText.setText(time.format(DateTimeFormatter.ofPattern("HH:mm")));
            handler.postDelayed(runnable, 5000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        curvedText = findViewById(R.id.time);


        View mainLayout = findViewById(R.id.main_layout);
        mainLayout.setOnGenericMotionListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_SCROLL &&
                    event.isFromSource(InputDeviceCompat.SOURCE_ROTARY_ENCODER)
            ) {
                if (gameFinished) {
                    return true;
                }
                float delta = -event.getAxisValue(MotionEventCompat.AXIS_SCROLL) *
                        ViewConfigurationCompat.getScaledVerticalScrollFactor(
                                ViewConfiguration.get(getApplicationContext()), getApplicationContext()
                        );

                if (new Date().getTime() - lastRotary < 500) {
                    currentRotation += delta;
                } else {
                    currentRotation = 0;
                }
                Log.i("Rotation", String.valueOf(currentRotation));


                if (currentFragment instanceof MainFragment) {

                    if (currentRotation > 0) {
                        ((MainFragment) currentFragment).updateOffsetPlayer1((int) (currentRotation / 384f * 70));
                    } else if (currentRotation < 0) {
                        ((MainFragment) currentFragment).updateOffsetPlayer2((int) (currentRotation / 384f * 70));
                    }

                }

                if (currentFragment instanceof RebuttalFragment) {
                    if (currentRotation > 0) {
                        ((RebuttalFragment) currentFragment).updateOffsetYes((int) (currentRotation / 384f * 70));
                    } else if (currentRotation < 0) {
                        ((RebuttalFragment) currentFragment).updateOffsetNo((int) (currentRotation / 384f * 70));
                    }
                }


                lastRotary = new Date().getTime();
                return true;
            }
            return false;
        });

        mainLayout.requestFocus();

        handler = new Handler();
        handler.post(runnable);

        AmbientModeSupport.attach(this);


    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {

        if (fragment instanceof RebuttalFragment) {
            currentFragment = fragment;
            ((RebuttalFragment) fragment).setRebuttingPlayer(player1Turn);
        } else if (fragment instanceof MainFragment) {
            currentFragment = fragment;
            ((MainFragment) currentFragment).updateScores(player1Score, player2Score);
        } else if (fragment instanceof WinnerFragment) {
            currentFragment = fragment;
            ((WinnerFragment) currentFragment).updateWinner(player1Won);
            ((WinnerFragment) fragment).setParentActivity(this);

        }
    }


    @Override
    public void player1Sink() {
        currentRotation = 0;
        player1Turn = false;
        player1Sinks += 1;
        setInRebuttalSeries();
    }

    @Override
    public void player2Sink() {
        currentRotation = 0;
        player2Sinks += 1;
        player1Turn = true;
        setInRebuttalSeries();
    }

    @Override
    public int getPlayer1Score() {
        return player1Score;
    }

    @Override
    public int getPlayer2Score() {
        return player2Score;
    }

    @Override
    public void rebuttalYes() {
        currentRotation = 0;
        currentRebuttalStreak += 1;
        ((RebuttalFragment) currentFragment).setRebuttalStreak(currentRebuttalStreak);


        if (player1Turn) {
            player1Sinks += 1;
            player1Rebuttals += 1;
        } else {
            player2Sinks += 1;
            player2Rebuttals += 1;
        }
        player1Turn = !player1Turn;
        ((RebuttalFragment) currentFragment).setRebuttingPlayer(player1Turn);
    }

    @Override
    public void rebuttalNo() {
        currentRotation = 0;
        currentRebuttalStreak = 0;
        if (player1Turn) {
            player2Score += 1;
        } else {
            player1Score += 1;
        }
        if (player1Score == 11 || player2Score == 11) {
            gameFinished = true;
            player1Won = player1Score == 11;
            showWinnerFragment();
            return;
        }
        endRebuttalSeries();
        player1Turn = !player1Turn;
    }

    private void setInRebuttalSeries() {
        if (!inRebuttalSeries) {
            showRebuttalFragment();
        }
        inRebuttalSeries = true;


    }

    private void showRebuttalFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainerView, RebuttalFragment.class, null);
        transaction.commit();
    }

    private void endRebuttalSeries() {
        inRebuttalSeries = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainerView, MainFragment.class, null);
        transaction.commit();
    }

    private void showWinnerFragment() {
        curvedText.setVisibility(View.GONE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragmentContainerView, WinnerFragment.class, null);
        transaction.commit();
    }

    public String getGameJson(){
        Game game = new Game();

        game.setPlayerPoints(player1Score);
        game.setPlayerSinks(player1Sinks);
        game.setOpponentPoints(player2Score);
        game.setOpponentSinks(player2Sinks);

        return  new Gson().toJson(game);
    }

    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new AmbientModeSupport.AmbientCallback() {
            @Override
            public void onEnterAmbient(Bundle ambientDetails) {

            }
        };
    }
}
