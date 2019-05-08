package com.rngg.models;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameMap<Z extends Zone, ZoneFormat> {

    protected HashMap<Z, ArrayList<Z>> neighbors;
    protected HashMap<Integer, Z> idMap;
    protected ShapeRenderer.ShapeType shapetype;

    public GameMap() {
        // reset id-counter on new mapgen for better concurrency
        Zone.resetIdCounter();
    }

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

    public Z _getZoneById(int id) {
        if (this.idMap == null && this.neighbors != null) {
            generateIdMap();
        }
        return this.idMap.get(id);
    }

    public void generateIdMap() {
        this.idMap = new HashMap<Integer, Z>();
        for (Z zone : _getZones()) {
            if (zone != null) {
                this.idMap.put(zone.getId(), zone);
            }
        }
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
        if (zone == null) return new ArrayList<Zone>();
        return new ArrayList<Zone>(neighbors.get(zone));
    }

    public boolean isNeighbors(Zone zone1, Zone zone2) {
        return _isNeighbors((Z) zone1, (Z) zone2);
    }

    public Zone getZoneById(int id) {
        return _getZoneById(id);
    }
}
