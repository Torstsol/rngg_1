package com.rngg.models;

import com.badlogic.gdx.graphics.Color;

public class Player {
    // TODO stub class
    private Color color;
    private String name;
    public final String playerId;
    public int randomNumber = -1;
    public int playerIndex = -1;
    public final boolean isLocal;

    public Player(String name, String playerId, boolean isLocal, Color color) {
        this.name = name;
        this.playerId = playerId;
        this.isLocal = isLocal;
        this.color = color;
    }

    public String getName(){
        return name;
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }
}
