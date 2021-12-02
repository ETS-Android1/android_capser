package com.globalcapsleague.app.fragments;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.activity.MainActivity;
import com.globalcapsleague.app.adapters.GameListAdapter;
import com.globalcapsleague.app.data.Fetch;
import com.globalcapsleague.app.enums.GameListType;
import com.globalcapsleague.app.models.GameFromApiDto;
import com.globalcapsleague.app.models.GameListObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    private List<GameListObject> gameListObjects;

    private Fetch fetch;
    private MainActivity mainActivity;
    private RecyclerView recyclerView;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        fetch = new Fetch(mainActivity);

        Type gameListType = new TypeToken<ArrayList<GameFromApiDto>>() {
        }.getType();

        fetch.fetchDashboardGames(response -> {
            List<GameFromApiDto> gameFromApiDtos= new Gson().fromJson(response, gameListType);
            gameListObjects = gameFromApiDtos.stream().map(object -> new GameListObject(GameListType.GAME,object)).collect(Collectors.toList());
            updateRecyclerView();
        });
    }

    private void updateRecyclerView(){
        if(gameListObjects ==null || recyclerView==null){
            return;
        }
        GameListObject listHeader = new GameListObject(GameListType.HEADER,"Latest games");
        gameListObjects.add(0,listHeader);
        GameListAdapter gameListAdapter = new GameListAdapter(mainActivity, gameListObjects);
        recyclerView.setAdapter(gameListAdapter);
        recyclerView.invalidate();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.dashboard_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        updateRecyclerView();
    }


}
