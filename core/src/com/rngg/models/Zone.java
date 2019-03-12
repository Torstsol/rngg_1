package com.rngg.models;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Zone {
    protected Player player;
    protected int units;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    abstract public void draw(ShapeRenderer sr);
}
