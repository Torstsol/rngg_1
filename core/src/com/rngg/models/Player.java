package com.rngg.models;

import com.badlogic.gdx.graphics.Color;

public class Player {
    // TODO stub class
    private Color color;
    private String name;
    public final String playerId;
    public final boolean isLocal;
    public final boolean isHost;

    public Player(String name, String playerId, boolean isLocal, boolean isHost, Color color) {
        this.name = name;
        this.playerId = playerId;
        this.isLocal = isLocal;
        this.color = color;
        this.isHost = isHost;
    }

    public String getName() {
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
