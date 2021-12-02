package com.globalcapsleague.app.models;

import com.globalcapsleague.app.enums.GameListType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameListObject {

    private GameListType type;

    private Object content;

}
