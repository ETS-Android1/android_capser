package com.globalcapsleague.app.models;

import com.example.globalcapsleagueapp.R;
import com.globalcapsleague.app.enums.GameMode;
import com.globalcapsleague.enums.GameType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameFromApiDto {

    private List<Object> gameEventList;
    protected List<GamePlayerStats> gamePlayerStats;
    private String team1Name;
    private String team2Name;
    private UUID player1;
    private UUID player2;
    private GameMode gameMode;
    private PlayerStatsDto player1Stats;
    private PlayerStatsDto player2Stats;
    private Date time;
    private GameType gameType;
    private int team1Score;
    private int team2Score;

    public GamePlayerStats getPlayer1Stats(){
        return gamePlayerStats.stream().filter(stats -> stats.getPlayerId().equals(player1)).findFirst().get();
    };

    public GamePlayerStats getPlayer2Stats(){
        return gamePlayerStats.stream().filter(stats -> stats.getPlayerId().equals(player2)).findFirst().get();
    };

    public static int getGameImageId(GameType gameType){
        switch (gameType){
            case DOUBLES:
                return R.drawable.doubles;
            case SINGLES:
                return R.drawable.singles;
            case UNRANKED:
                return R.drawable.unranked;
            case EASY_CAPS:
                return R.drawable.easy_caps;
        }
        return 0;
    }

    public static String getGameString(GameType gameType){
        switch (gameType){
            case DOUBLES:
                return "Doubles";
            case SINGLES:
                return "Singles";
            case UNRANKED:
                return "Unranked";
            case EASY_CAPS:
                return "Easy Caps";
        }
        return "";
    }
}
