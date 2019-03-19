package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.rngg.configuration.GamePreferences;
import com.rngg.utils.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class GameModel {
    /*
        TODO this is "hardcoded" in a lot of ways, for instance using SquareMap and SquareZone.
        This should be changed
    */

    public int playerScore = 0;
    private SquareMap map;
    private Player[] players;
    private int playerIndex;
    private SquareZone attacker;
    private int[] contiguousAreas;
    private RNG rng;
    private int maxUnits = 8;
    private GamePreferences pref;

    // defense strategies
    public String DEFEND_ALL = "DEFEND_ALL", DEFEND_CORE = "DEFEND_CORE", DEFEND_FRONTIER = "DEFEND_FRONTIER";

    public GameModel(int numPlayers) {
        pref = new GamePreferences();
        this.players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player("Player" + i, "90238049", true, pref.getColorArray().get(i));
        }
        this.playerIndex = 0;
        this.contiguousAreas = new int[numPlayers];
        this.map = new SquareMap(9, 16, players);
        this.updateAreas();
        this.rng = RNG.getInstance();
    }

    public SquareMap getMap() {
        return this.map;
    }

    public void click(Vector3 coords) {
        SquareZone temp = map.screenCoordToZone(new Vector2(coords.x, coords.y));
        if (temp.getPlayer() == this.currentPlayer()) {
            if (temp.getUnits() <= 1) {
                Gdx.app.log(this.getClass().getSimpleName(), temp.toString() + " has too few units to attack");
                return;
            }
            if (temp.getUnits() <= 1) {
                Gdx.app.log(this.getClass().getSimpleName(), temp.toString() + " has too few units to attack");
                return;
            }
            if (this.attacker != null) {
                this.attacker.unClick();
            }
            this.attacker = temp;
            this.attacker.click();
            Gdx.app.log(this.getClass().getSimpleName(), "Set " + this.attacker.toString() + " as attacker");
        } else {
            if (attacker == null) {
                Gdx.app.log(this.getClass().getSimpleName(), temp.toString() + " does not belong to " + this.currentPlayer());
                return;
            }
            if (!map.isNeighbors(this.attacker, temp)) {
                Gdx.app.log(this.getClass().getSimpleName(), temp.toString() + " and " + attacker.toString() + " are not neighbors");
                return;
            }
            this.attack(attacker, temp);
        }
    }

    public int attack(SquareZone attacker, SquareZone defender) {
        /*
        Returns
            -1 if defender defends
             0 if attack is invalid
             1 if attacker wins
        */

        attacker.unClick();
        defender.unClick();
        this.attacker = null;

        Gdx.app.log(this.getClass().getSimpleName(),
                String.format("%s is attacking %s", attacker.getPlayer().toString(), defender.getPlayer().toString())
        );

        this.rng.roll(attacker.getUnits());
        float attackRoll = rng.valueFromRoll();
        Gdx.app.log(this.getClass().getSimpleName(),
                attacker.toString() + " rolled " + attackRoll + " (" + Arrays.toString(rng.labelFromRoll()) + ")");

        this.rng.roll(defender.getUnits());
        float defendRoll = rng.valueFromRoll();
        Gdx.app.log(this.getClass().getSimpleName(),
                defender.toString() + " rolled " + defendRoll + " (" + Arrays.toString(rng.labelFromRoll()) + ")");

        boolean attackerWon = attackRoll > defendRoll;

        if (attackerWon) {
            Gdx.app.log(this.getClass().getSimpleName(), "Attacker won");
            defender.setUnits(attacker.getUnits() - 1);
            defender.setPlayer(attacker.getPlayer());
            this.updateAreas();
            // TODO check if defender is alive
            // TODO check if game is over
        } else {
            Gdx.app.log(this.getClass().getSimpleName(), "Attacker lost");
        }
        attacker.setUnits(1);
        return attackerWon ? 1 : -1;
    }

    public Player currentPlayer() {
        return this.players[playerIndex];
    }

    public int getPlayerIndex() {
        return this.playerIndex;
    }

    public void setPlayer(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < this.players.length) {
            this.playerIndex = playerIndex;
            Gdx.app.log(
                    this.getClass().getSimpleName(),
                    "Set playerIndex to " + playerIndex + ", currentPlayer is " + currentPlayer()
            );
        } else {
            Gdx.app.error(
                    this.getClass().getSimpleName(),
                    "Illegal player index given (" + playerIndex + " is not in [0, " + this.players.length + "])");
        }
    }


    public void updateAreas() {
        /*
        Method for calculating contiguous areas per player
        Kinda funky with lots of loops, but it does at least work
         */
        // for each player
        for (int i = 0; i < this.players.length; i++) {
            Player player = this.players[i];
            int max = 0;
            // TODO hardcoded for SquareZone
            // total graph for this player
            ArrayList<SquareZone> graph = map.getPlayerZones(player);
            // subgraph for search
            ArrayList<SquareZone> subgraph = new ArrayList<SquareZone>();
            // while the graph is not fully explored
            while (!graph.isEmpty()) {
                boolean changed;
                // choose the next node in the list as the start of a new search
                subgraph.add(graph.get(0));
                // while this subgraph is not fully explored
                do {
                    // remember where to start next loop to avoid redundant checks
                    int start = subgraph.size() - 1;
                    // assume we found nothing new
                    changed = false;
                    // for each member of the subgraph
                    for (int j = start; j < subgraph.size(); j++) {
                        SquareZone subgraphNode = subgraph.get(j);
                        // for each neighbor of that member
                        for (SquareZone neighbor : map.getNeighbors(subgraphNode)) {
                            // if the neighbor should be a part of the subgraph but isn't yet
                            if (neighbor.getPlayer().equals(player) && !subgraph.contains(neighbor)) {
                                // add the neighbor, note that we found a change
                                subgraph.add(neighbor);
                                changed = true;
                            }
                        }
                    }
                } while (changed);
                // update max
                max = Math.max(max, subgraph.size());
                // remove subgraph from graph
                graph.removeAll(subgraph);
                // reset subgraph
                subgraph.clear();
            }
            this.contiguousAreas[i] = max;
        }
        Gdx.app.log(
                this.getClass().getSimpleName(),
                "Updated contiguous area count: " + Arrays.toString(this.contiguousAreas)
        );
    }

    public void defend(int playerIndex, String strategy) {
        // generate a list of lists
        // take from the first list until it is exhausted or units are empty
        // continue until no lists remain or no units remain
        // TODO hard-coded for squarezone
        // total number of units to distribute
        int units = this.contiguousAreas[playerIndex];
        Player player = this.players[playerIndex];
        // this is the list of lists from which to get zones
        ArrayList<ArrayList<SquareZone>> zones = new ArrayList<ArrayList<SquareZone>>();
        if (strategy.equals(DEFEND_ALL)) {
            // defend all = no ordering, one sublist
            zones.add(map.getPlayerZones(player));
        } else {
            // sublists are based on ratio of friendly neighbors
            // 1 = all 4 neighbors are same player, 0 = all are enemy
            // first, create a hashmap of the ratio
            HashMap<Float, ArrayList<SquareZone>> hashMap = new HashMap<Float, ArrayList<SquareZone>>();
            for (SquareZone z : map.getPlayerZones(player)) {
                ArrayList<SquareZone> neighbors = map.getNeighbors(z);
                float friendlyNeighborRatio = 0;
                for (SquareZone neighbor : neighbors) {
                    if (neighbor.getPlayer().equals(player)) {
                        friendlyNeighborRatio++;
                    }
                }
                friendlyNeighborRatio /= neighbors.size();
                if (!hashMap.containsKey(friendlyNeighborRatio)) {
                    hashMap.put(friendlyNeighborRatio, new ArrayList<SquareZone>());
                }
                hashMap.get(friendlyNeighborRatio).add(z);
            }
            // next, order the numbers based on strategy
            ArrayList<Float> nums = new ArrayList<Float>(hashMap.keySet());
            Collections.sort(nums);
            if (strategy.equals(DEFEND_CORE)) {
                // highest values first
                Collections.reverse(nums);
            }
            // add sublists to list
            for (float num : nums) {
                zones.add(hashMap.get(num));
            }
        }
        Random rand = new Random();
        ArrayList<SquareZone> currentList = null;
        while (units > 0 && !zones.isEmpty()) {
            if (currentList == null) {
                // get a new sublist
                currentList = zones.get(0);
            }
            SquareZone zone = currentList.get(rand.nextInt(currentList.size()));
            // if the zone is saturated, remove it from the sublist
            // if the sublist is empty, remove it from the list
            if (zone.getUnits() >= this.maxUnits) {
                currentList.remove(zone);
                if (currentList.isEmpty()) {
                    zones.remove(currentList);
                    currentList = null;
                }
                continue;
            }
            // zone is legal, increment its units and decrement units to hand out
            zone.incrementUnits();
            units--;
        }
    }

    public void nextPlayer(){
        this.playerIndex = (this.playerIndex + 1)%(players.length);
        Gdx.app.debug(this.getClass().getSimpleName(), "Player " + playerIndex + " is now playing");
    }
}
