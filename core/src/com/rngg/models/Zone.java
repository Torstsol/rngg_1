package com.rngg.models;

import com.rngg.utils.RNG;

public abstract class Zone {
    protected Player player;
    protected int units;
    protected boolean clicked = false;

    public Zone(Player player, int maxUnits) {
        this.player = player;
        // + 1 because nextInt upper bound is exclusive
        this.units = RNG.nextInt(1, maxUnits + 1);
    }

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

    public void incrementUnits() {
        this.units++;
    }

    public void click() {
        this.clicked = true;
    }

    public void unClick() {
        this.clicked = false;
    }

    public boolean getClicked() {
        return this.clicked;
    }
}
