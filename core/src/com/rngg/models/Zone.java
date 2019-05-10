package com.rngg.models;

import com.rngg.utils.RNG;

public abstract class Zone implements Comparable<Zone> {
    protected Player player;
    protected static int idCounter = 0;
    protected int units, id;
    protected boolean clicked = false;

    public Zone(Player player, int maxUnits) {
        this.id = idCounter++;
        this.player = player;
        // + 1 because nextInt upper bound is exclusive
        this.units = RNG.nextInt(1, maxUnits + 1);
    }

    public Zone(Player player, int maxUnits, Integer units) {
        this.id = idCounter++;
        this.player = player;
        this.units = units;
    }

    public static void resetIdCounter() {
        Zone.idCounter = 0;
    }

    public int getId() {
        return this.id;
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

    public int compareTo(Zone other) {
        return Integer.compare(this.id, other.id);
    }
}
