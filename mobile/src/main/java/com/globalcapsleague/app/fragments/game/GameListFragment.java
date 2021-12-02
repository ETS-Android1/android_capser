package com.globalcapsleague.app.fragments.game;

import android.os.Bundle;
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
import com.globalcapsleague.enums.GameType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameListFragment extends Fragment {

    private MainActivity mainActivity;
    private Fetch fetch;
    private RecyclerView recyclerView;


    public GameListFragment(){
        super(R.layout.fragment_game_page);
    }



    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        fetch = new Fetch(mainActivity);

        Type gameListType = new TypeToken<ArrayList<GameFromApiDto>>() {
        }.getType();


    }

    private void setGameType(GameType gameType){

        List<GameListObject> gameListObjects = new ArrayList<>();
        Type gameListType = new TypeToken<ArrayList<GameFromApiDto>>() {
        }.getType();

        fetch.fetchGamesOfType(gameType,response -> {
            List<GameFromApiDto> gameFromApiDtos= new Gson().fromJson(response, gameListType);
            gameListObjects.addAll(gameFromApiDtos.stream().map(object -> new GameListObject(GameListType.GAME,object)).collect(Collectors.toList()));
        });

        GameListObject listHeader = new GameListObject(GameListType.HEADER,GameFromApiDto.getGameString(gameType));
        gameListObjects.add(0,listHeader);
        GameListAdapter gameListAdapter = new GameListAdapter(mainActivity, gameListObjects);
        recyclerView.setAdapter(gameListAdapter);
        recyclerView.invalidate();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.dashboard_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
    }

}
