package com.rngg.models;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class Bot extends Player {
    private GameModel model;

    public Bot(String name, String playerId, boolean isLocal, GameModel model) {
        super(name, playerId, isLocal);
        this.model = model;
    }

    public void act() {
        Gdx.app.log(this.getClass().getSimpleName(), "BOT " + this + " acting");
        // attacks using the following strategy:
        // 1. check all of players zones
        // 2. check all neighbors of those zones
        // 3. if a neighbor belongs to another player, check if you should attack
        // 4. if bot attacks and wins, add new zone to list of zones
        // 5. if some zone was added in step 4, repeat
        boolean changed;
        ArrayList<Zone> zones = model.getMap().getPlayerZones(this);
        do {
            // assume no zones captured
            changed = false;
            // keep track of zones checked, there is no need to check them again later
            ArrayList<Zone> deleteList = new ArrayList<Zone>();
            for (int i = 0; i < zones.size(); i++) {
                Zone zone = zones.get(i);
                deleteList.add(zone);
                ArrayList<Zone> neighbors = model.getMap().getNeighbors(zone);
                for (Zone neighbor : neighbors) {
                    if (!neighbor.getPlayer().equals(this) && shouldAttack(zone, neighbor)) {
                        int result = model.attack(zone, neighbor);
                        // result > 0 means bot won
                        if (result > 0) {
                            zones.add(neighbor);
                            changed = true;
                        }
                    }
                }
            }
            // remove all visited zones
            zones.removeAll(deleteList);
        } while (changed);
        // bots only act when it is their turn, thus we defend using the current playerIndex
        // TODO bots always defend using DEFEND_ALL
        model.defend(model.getPlayerIndex(), GameModel.DEFEND_ALL);
        model.nextPlayer();
    }

    public boolean shouldAttack(Zone attacker, Zone defender) {
        // method for determining whether or not to attack
        // TODO current bot is a warmonger and will ALWAYS try to attack, regardless of units
        return true;
    }
}
