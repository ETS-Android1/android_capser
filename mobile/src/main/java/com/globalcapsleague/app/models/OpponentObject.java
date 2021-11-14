package com.globalcapsleague.app.models;

import androidx.annotation.NonNull;

public class OpponentObject {

    public OpponentObject(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name;
    private String id;

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
