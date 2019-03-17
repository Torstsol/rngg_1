package com.rngg.models;

import com.badlogic.gdx.graphics.Color;

public class Player {
    // TODO stub class
    private Color color;
    private String name;

    public Player(String name) {
        this.name = name;
        // TODO get colors from config
        this.color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
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
