package com.globalcapsleague.app.models;

public class Game {

    private int playerPoints;
    private int playerSinks;

    private int opponentPoints;
    private int opponentSinks;

    private int playerRebuttals;
    private int opponentRebuttals;

    public int getPlayerRebuttals() {
        return playerRebuttals;
    }

    public void setPlayerRebuttals(int playerRebuttals) {
        this.playerRebuttals = playerRebuttals;
    }

    public int getOpponentRebuttals() {
        return opponentRebuttals;
    }

    public void setOpponentRebuttals(int opponentRebuttals) {
        this.opponentRebuttals = opponentRebuttals;
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }

    public int getPlayerSinks() {
        return playerSinks;
    }

    public void setPlayerSinks(int playerSinks) {
        this.playerSinks = playerSinks;
    }

    public int getOpponentPoints() {
        return opponentPoints;
    }

    public void setOpponentPoints(int opponentPoints) {
        this.opponentPoints = opponentPoints;
    }

    public int getOpponentSinks() {
        return opponentSinks;
    }

    public void setOpponentSinks(int opponentSinks) {
        this.opponentSinks = opponentSinks;
    }
}
