package com.globalcapsleague.app.models;

import java.util.UUID;

import lombok.Data;

@Data
public class GamePlayerStats {

    private UUID playerId;

    private int score;
    private int sinks;
    private int rebuttals;
    private float pointsChange;
    private float beersDowned;
    private boolean nakedLap;
}
