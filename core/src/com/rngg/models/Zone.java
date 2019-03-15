package com.rngg.models;

import com.rngg.views.GameView;

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

    abstract public void draw(GameView view);

    abstract public void drawText(GameView view);
}
