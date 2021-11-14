package com.globalcapsleague.app.models;

import com.globalcapsleague.app.enums.GameMode;

import java.util.List;

public class GameDto {

    private List<Object> gameEventList;


    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public PlayerStatsDto getPlayer1Stats() {
        return player1Stats;
    }

    public void setPlayer1Stats(PlayerStatsDto player1Stats) {
        this.player1Stats = player1Stats;
    }

    public PlayerStatsDto getPlayer2Stats() {
        return player2Stats;
    }

    public void setPlayer2Stats(PlayerStatsDto player2Stats) {
        this.player2Stats = player2Stats;
    }

    private GameMode gameMode;

    private PlayerStatsDto player1Stats;

    private PlayerStatsDto player2Stats;

    public List<Object> getGameEventList() {
        return gameEventList;
    }

    public void setGameEventList(List<Object> gameEventList) {
        this.gameEventList = gameEventList;
    }
}
