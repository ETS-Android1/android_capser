package com.globalcapsleague.app.activity;

import android.os.Bundle;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.fragments.HomeFragment;

public class MainActivity extends GclBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_holder);
        setupActionBar();
        navigationView.setCheckedItem(R.id.nav_home);

        setFragment(HomeFragment.class);


    }


}