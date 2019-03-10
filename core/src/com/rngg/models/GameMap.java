package com.rngg.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameMap {
    private HashMap<Zone, ArrayList<Zone>> zones;

    public GameMap(HashMap<Zone, ArrayList<Zone>> zones) {
        this.zones = zones;
    }

    abstract public Zone screenCoordToZone(Vector2 coords);

    public ArrayList<Zone> getPlayerZones(Player player) {
        ArrayList<Zone> ret = new ArrayList<Zone>();
        for (Zone zone : zones.keySet()) {
            if (zone.getPlayer().equals(player)) {
                ret.add(zone);
            }
        }
        return ret;
    }

    public ArrayList<Zone> getNeighbors(Zone zone) {
        return zones.get(zone);
    }

    private boolean isNeighbors(Zone zone1, Zone zone2) {
        return getNeighbors(zone1).contains(zone2);
    }

    public void draw() {
        for (Zone zone : zones.keySet()) {
            zone.draw();
        }
    }

    public int attack(Zone attacker, Zone defender) {
        // TODO move this to another class
        /*
        Returns
            -1 if defender defends
             0 if attack is invalid
             1 if attacker wins
        */
        Gdx.app.log(this.getClass().getSimpleName(),
            String.format("%s is attacking %s", attacker.getPlayer().toString(), defender.getPlayer().toString())
        );
        if (isNeighbors(attacker, defender)) {
            Gdx.app.log(this.getClass().getSimpleName(), "Attacker and defender zones were not neighbors");
            return 0;
        }
        if (attacker.getPlayer().equals(defender.getPlayer())) {
            Gdx.app.log(this.getClass().getSimpleName(), "Friendly fire will not be tolerated");
            return 0;
        }

        // TODO dice rolls
        boolean attackerWon = true;

        if (attackerWon) {
            Gdx.app.log(this.getClass().getSimpleName(), "Attacker won");
            defender.setUnits(attacker.getUnits() - 1);
            defender.setPlayer(attacker.getPlayer());
            // TODO check if defender is alive
            // TODO check if game is over
        } else {
            Gdx.app.log(this.getClass().getSimpleName(), "Attacker lost");
        }
        attacker.setUnits(1);
        return attackerWon ? 1 : -1;
    }
}
