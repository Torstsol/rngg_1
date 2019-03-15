package com.rngg.models;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.rngg.views.GameView;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameMap<Z extends Zone> {

    protected HashMap<Z, ArrayList<Z>> neighbors;
    protected ShapeRenderer.ShapeType shapetype;

    abstract public Z screenCoordToZone(Vector2 coords);

    public ArrayList<Z> getPlayerZones(Player player) {
        ArrayList<Z> ret = new ArrayList<Z>();
        for (Z zone : neighbors.keySet()) {
            if (zone.getPlayer().equals(player)) {
                ret.add(zone);
            }
        }
        return ret;
    }

    public ArrayList<Z> getNeighbors(Z zone) {
        return neighbors.get(zone);
    }

    public boolean isNeighbors(Z zone1, Z zone2) {
        return getNeighbors(zone1).contains(zone2);
    }

    public void draw(GameView view) {

        view.getSR().begin(shapetype);
        for (Z zone : neighbors.keySet()) {
            zone.draw(view);
        }
        view.getSR().end();

        view.getBatch().begin();
        for (Z zone : neighbors.keySet()) {
            zone.drawText(view);
        }
        view.getBatch().end();
    }
}

abstract class Zone {
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