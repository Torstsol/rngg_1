package com.rngg.models;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class GameMap<Z extends Zone, ZoneFormat> {

    protected HashMap<Z, ArrayList<Z>> neighbors;
    protected ShapeRenderer.ShapeType shapetype;

    abstract public ZoneFormat decodeZoneJSON(JsonValue zones);

    // Underscore methods returns Z's, for use in Map and MapRenderer
    abstract public Z _screenCoordToZone(Vector2 coords);

    public ArrayList<Z> _getZones() {
        return new ArrayList<Z>(neighbors.keySet());
    }

    public ArrayList<Z> _getPlayerZones(Player player) {
        ArrayList<Z> ret = new ArrayList<Z>();
        for (Z zone : neighbors.keySet()) {
            if (zone == null) continue;

            if (zone.getPlayer().equals(player)) {
                ret.add(zone);
            }
        }
        return ret;
    }

    public ArrayList<Z> _getNeighbors(Z zone) {
        return neighbors.get(zone);
    }

    public boolean _isNeighbors(Z zone1, Z zone2) {
        return getNeighbors(zone1).contains(zone2);
    }

    // Non-underscore methods cast Z up to Zone, and are used in the gameModel (and gameController)
    public Zone screenCoordToZone(Vector2 coords) {
        return _screenCoordToZone(coords);
    }

    public ArrayList<Zone> getZones() {
        return new ArrayList<Zone>(neighbors.keySet());
    }

    public ArrayList<Zone> getPlayerZones(Player player) {
        return new ArrayList<Zone>(_getPlayerZones(player));
    }

    public ArrayList<Zone> getNeighbors(Zone zone) {
        return new ArrayList<Zone>(neighbors.get(zone));
    }

    public boolean isNeighbors(Zone zone1, Zone zone2) {
        return _isNeighbors((Z) zone1, (Z) zone2);
    }
}
