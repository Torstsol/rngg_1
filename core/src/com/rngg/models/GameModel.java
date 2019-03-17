package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.rngg.utils.RNG;

import java.util.ArrayList;
import java.util.Arrays;

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

    public GameModel(int numPlayers) {
        this.players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player("Player" + i);
        }
        this.playerIndex = 0;
        this.contiguousAreas = new int[numPlayers];
        this.map = new SquareMap(3, 6, players);
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
}
