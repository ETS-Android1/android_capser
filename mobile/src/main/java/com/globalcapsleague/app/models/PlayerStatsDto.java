package com.globalcapsleague.app.models;

public class PlayerStatsDto {

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSinks() {
        return sinks;
    }

    public void setSinks(int sinks) {
        this.sinks = sinks;
    }



    private String playerId;

    private int score;

    private int sinks;

}
