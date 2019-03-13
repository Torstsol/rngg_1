package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

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

    private boolean isNeighbors(Z zone1, Z zone2) {
        return getNeighbors(zone1).contains(zone2);
    }

    public void draw(ShapeRenderer sr) {
        sr.begin(shapetype);
        for (Z zone : neighbors.keySet()) {
            zone.draw(sr);
        }
        sr.end();
    }

    public int attack(Z attacker, Z defender) {
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
            Gdx.app.log(this.getClass().getSimpleName(), "Attacker and defender neighbors were not neighbors");
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
