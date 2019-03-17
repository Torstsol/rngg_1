package com.rngg.models;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameMap<Z extends Zone> {

    protected HashMap<Z, ArrayList<Z>> neighbors;
    protected ShapeRenderer.ShapeType shapetype;

    abstract public Z screenCoordToZone(Vector2 coords);

    public ArrayList<Z> getZones() {
        return new ArrayList<Z>(neighbors.keySet());
    }

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
}
