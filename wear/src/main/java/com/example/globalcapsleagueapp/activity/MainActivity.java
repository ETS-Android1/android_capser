package com.example.globalcapsleagueapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.core.view.InputDeviceCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.globalcapsleagueapp.activity.game.GameActivity;
import com.example.globalcapsleagueapp.activity.news.NewsActivity;
import com.example.globalcapsleagueapp.lists.CustomScrollingLayoutCallback;
import com.example.globalcapsleagueapp.lists.ListAdapter;
import com.example.globalcapsleagueapp.lists.MenuObject;
import com.example.globalcapsleagueapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WearableRecyclerView recyclerView = findViewById(R.id.recycler_launcher_view);
        recyclerView.setEdgeItemsCenteringEnabled(true);

        Intent intent = new Intent(this, GameActivity.class);
        Intent newsIntent = new Intent(this, NewsActivity.class);



        List<MenuObject> menuObjects = new ArrayList<>();

        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.HEADING,"Global Caps League"));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.ELEMENT,"News",R.drawable.home,v -> startActivity(newsIntent)));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.ELEMENT,"Latest games",R.drawable.game));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.ELEMENT,"Tournaments",R.drawable.tournament));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.HEADING,"Games"));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.ELEMENT,"Add game", R.drawable.add,v -> startActivity(intent)));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.ELEMENT,"Easy Caps",R.drawable.easy_caps));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.ELEMENT,"Singles",R.drawable.singles));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.ELEMENT,"Doubles",R.drawable.doubles));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.ELEMENT,"Unranked",R.drawable.unranked));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.HEADING,"Misc"));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.ELEMENT,"Rules",R.drawable.gavel));
        menuObjects.add(new MenuObject(MenuObject.MenuObjectType.ELEMENT,"Commandments",R.drawable.ten_commandments));


        recyclerView.setLayoutManager(new WearableLinearLayoutManager(this,new CustomScrollingLayoutCallback()));

        recyclerView.setAdapter(new ListAdapter(menuObjects));
        recyclerView.requestFocus();



        recyclerView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_SCROLL &&
                        event.isFromSource(InputDeviceCompat.SOURCE_ROTARY_ENCODER)
                ) {
                    // Don't forget the negation here
                    float delta = -event.getAxisValue(MotionEventCompat.AXIS_SCROLL) *
                            ViewConfigurationCompat.getScaledVerticalScrollFactor(
                                    ViewConfiguration.get(getApplicationContext()), getApplicationContext()
                            );

                    // Swap these axes to scroll horizontally instead
                    ((RecyclerView)v).smoothScrollBy(0, Math.round(delta));

                    return true;
                }
                return false;
            }
        });


    }


}