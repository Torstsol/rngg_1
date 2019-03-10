package com.rngg.models;

public abstract class Zone {
    private Player player;
    private int units;

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

    abstract public void draw();
}
