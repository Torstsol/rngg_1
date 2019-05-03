package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.rngg.configuration.GamePreferences;
import com.rngg.services.IPlayServices;
import com.rngg.services.Message;
import com.rngg.services.RealtimeListener;
import com.rngg.services.IPlayServices;
import com.rngg.services.Message;
import com.rngg.utils.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class GameModel implements RealtimeListener{
    public int playerScore = 0;
    private GameMap map;
    private ArrayList<Player> players;
    private int playerIndex;
    private Zone attacker;
    private int[] contiguousAreas;
    private RNG rng;
    private int maxUnits = 8;
    private int numPlayers;
    private GamePreferences pref = GamePreferences.getInstance();
    private boolean inGameMenuOpen;
    private IPlayServices sender;
    public Player host;
    public Player localPlayer;
    public boolean localGame = false;

    public static final String HEX = "HEX", SQUARE = "SQUARE";

    private float attackRoll, defendRoll;
    private String[] attackValues, defendValues;

    // defense strategies
    public static final String DEFEND_ALL = "DEFEND_ALL", DEFEND_CORE = "DEFEND_CORE", DEFEND_FRONTIER = "DEFEND_FRONTIER";

    public GameModel(ArrayList<Player> players, String mapFileName, IPlayServices sender) {
        this.playerIndex = 0;
        this.attackRoll = 0;
        this.defendRoll = 0;
        this.sender = sender;

        //support for localgame, generates players or "bots"
        if(players == null){
            this.localGame = true;
            this.players = new ArrayList<Player>();
            this.players.add(new Player("You", "6969", true, true, pref.getColorArray().get(0)));
            this.host = this.players.get(0);
            this.localPlayer = this.players.get(0);
            for (int i = 1; i < 4; i++) {
                this.players.add(new Player("BOT" + i, "6969", true, false, pref.getColorArray().get(i)));
            }
        }
        else{
            this.players = players;
            for (int i = 0; i < players.size(); i++) {
                if(players.get(i).isHost == true){
                    this.host = players.get(i);
                    System.out.println("HOSTNAME: " + players.get(i).getName());
                    System.out.println("HOST_ID: " + players.get(i).playerId);
                }
                if (players.get(i).isLocal == true){
                    this.localPlayer = players.get(i);
                    System.out.println("LocalNAME: " + players.get(i).getName());
                    System.out.println("Local_ID: " + players.get(i).playerId);
                }
            }

        }
        this.rng = RNG.getInstance();
        this.contiguousAreas = new int[this.players.size()];

        this.setMap(mapFileName);

        //check if proto-host, if true, generate seed, shuffle playerlist, and broadcast seed
        if(this.localPlayer.isHost){
            long shuffleSeed = this.rng.getSeed();
            Collections.shuffle(this.players, new Random(shuffleSeed));
            System.out.println(this.players.toString());

            if(!localGame){
                System.out.println("This unit believes its host and sends out order");
                //broadcast order seed to everyone
                Message message = new Message(new byte[512],"",0);
                message.putString("ORDER");
                message.putLong(shuffleSeed);
                sender.sendToAllReliably(message.getData());
            }
            //if the proto-host ends up as settings-host also, it has to broadcast the settings
            if(!localGame && this.localPlayer.playerId.equals(players.get(0).playerId)){
                System.out.println("This unit believes its host and settingshost, and sends out settings");
                sendSettings();
            }
        }



    }

    public GameModel(ArrayList<Player> players, IPlayServices sender) {
        this(players, "", sender);
    }

    public void setMap(String fileName) {
        if (fileName.length() > 0) {
            this.map = loadMap(fileName);
        } else {
            //initializePlayerAndAreas();
            this.map = new SquareMap(9, 16, players);
        }

        this.updateAreas();
    }

    private GameMap loadMap(String fileName) {
        String mapType = "";
        int totalCols = -1;
        int totalRows = -1;
        int maxPlayers = -1;
        boolean randomPlayers = false;
        int offset = 0;
        JsonValue zones = null;

        JsonValue json = new JsonReader().parse(Gdx.files.internal(fileName).readString());

        for (JsonValue value : json) {
            if (value.name.equals("mapType")) {
                mapType = value.asString();
            } else if (value.name.equals("totalCols")) {
                totalCols = value.asInt();
            } else if (value.name.equals("totalRows")) {
                totalRows = value.asInt();
            } else if (value.name.equals("maxPlayers")) {
                maxPlayers = value.asInt();
            } else if (value.name.equals("randomPlayers")) {
                randomPlayers = value.asBoolean();
            } else if (value.name.equals("offset")) {
                offset = value.asInt();
            } else if (value.name.equals("zones")) {
                zones = value;
            }
        }

        if (!randomPlayers || this.numPlayers > maxPlayers) {
            this.numPlayers = maxPlayers;
        }

        //initializePlayerAndAreas();

        if (mapType.equals("SquareMap")) {
            return new SquareMap(totalRows, totalCols, this.players, randomPlayers, zones);
        } else if (mapType.equals("HexMap")) {
            return new HexMap(totalRows, totalCols, this.players, randomPlayers, zones, offset);
        }

        this.updateAreas();
        this.rng = RNG.getInstance();
        this.attackRoll = 0;
        this.defendRoll = 0;
        this.inGameMenuOpen = false;
        return new SquareMap(9, 16, this.players);
    }

    public GameMap getMap() {
        return this.map;
    }

    public Player getPlayer(int index){
        return players.get(index);
    }

    public void click(Vector3 coords) {
        Zone temp = map.screenCoordToZone(new Vector2(coords.x, coords.y));

        if (temp == null) return;

        if (temp.getPlayer() == this.currentPlayer()) {
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

    public int attack(Zone attacker, Zone defender) {
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
        attackRoll = rng.valueFromRoll();
        attackValues = rng.labelFromRoll();
        Gdx.app.log(this.getClass().getSimpleName(),
                attacker.toString() + " rolled " + attackRoll + " (" + Arrays.toString(rng.labelFromRoll()) + ")");

        this.rng.roll(defender.getUnits());
        defendRoll = rng.valueFromRoll();
        defendValues = rng.labelFromRoll();
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
        return this.players.get(playerIndex);
    }

    public int getPlayerIndex() {
        return this.playerIndex;
    }

    public void setPlayer(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < this.players.size()) {
            this.playerIndex = playerIndex;
            Gdx.app.log(
                    this.getClass().getSimpleName(),
                    "Set playerIndex to " + playerIndex + ", currentPlayer is " + currentPlayer()
            );
        } else {
            Gdx.app.error(
                    this.getClass().getSimpleName(),
                    "Illegal player index given (" + playerIndex + " is not in [0, " + this.players.size() + "])");
        }
    }

    public void updateAreas() {
        /*
        Method for calculating contiguous areas per player
        Kinda funky with lots of loops, but it does at least work
         */
        // for each player
        for (int i = 0; i < this.players.size(); i++) {
            Player player = this.players.get(i);
            int max = 0;
            // total graph for this player
            ArrayList<Zone> graph = new ArrayList<Zone>(map.getPlayerZones(player));
            // subgraph for search
            ArrayList<Zone> subgraph = new ArrayList<Zone>();
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
                        Zone subgraphNode = subgraph.get(j);
                        // for each neighbor of that member
                        for (Zone neighbor : (ArrayList<Zone>) map.getNeighbors(subgraphNode)) {
                            // if the neighbor should be a part of the subgraph but isn't yet
                            if (neighbor == null) continue;

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
        // total number of units to distribute
        int units = this.contiguousAreas[playerIndex];
        Player player = this.players.get(playerIndex);
        // this is the list of lists from which to get zones
        ArrayList<ArrayList<Zone>> zones = new ArrayList<ArrayList<Zone>>();
        if (strategy.equals(DEFEND_ALL)) {
            // defend all = no ordering, one sublist
            zones.add(map.getPlayerZones(player));
        } else {
            // sublists are based on ratio of friendly neighbors
            // 1 = all 4 neighbors are same player, 0 = all are enemy
            // first, create a hashmap of the ratio
            HashMap<Float, ArrayList<Zone>> hashMap = new HashMap<Float, ArrayList<Zone>>();
            for (Zone z : (ArrayList<Zone>) map.getPlayerZones(player)) {
                ArrayList<Zone> neighbors = map.getNeighbors(z);
                float friendlyNeighborRatio = 0;
                for (Zone neighbor : neighbors) {
                    if (neighbor == null) continue;

                    if (neighbor.getPlayer().equals(player)) {
                        friendlyNeighborRatio++;
                    }
                }
                friendlyNeighborRatio /= neighbors.size();
                if (!hashMap.containsKey(friendlyNeighborRatio)) {
                    hashMap.put(friendlyNeighborRatio, new ArrayList<Zone>());
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
        ArrayList<Zone> currentList = null;
        while (units > 0 && !zones.isEmpty()) {
            if (currentList == null) {
                // get a new sublist
                currentList = zones.get(0);
            }
            Zone zone = RNG.choice(currentList);
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
        this.attackRoll = 0;
        this.defendRoll = 0;

        if (this.attacker != null) {
            this.attacker.unClick();
        }
        this.playerIndex = (this.playerIndex + 1)%(players.size());
        Gdx.app.debug(this.getClass().getSimpleName(), "Player " + playerIndex + " is now playing");
    }

    public int getAttackRoll() {
        return (int) attackRoll;
    }

    public String[] getAttackValues() {
        return attackValues;
    }

    public int getDefendRoll() {
        return (int) defendRoll;
    }

    public String[] getDefendValues() {
        return defendValues;
    }

    public boolean isInGameMenuOpen() {
        return inGameMenuOpen;
    }

    public void updateInGameMenu() {
        this.inGameMenuOpen = !this.inGameMenuOpen;
    }

    public void applySettings(Message settings, boolean sentBySelf) {
        Gdx.app.log(this.getClass().getSimpleName(), "Receiving settings");

        if (sentBySelf) {
            // pop the header from the message, we don't need it
            // if the message was received via network, the header is popped in handleDataReceived
            String header = settings.getString();
            Gdx.app.log(this.getClass().getSimpleName(), "Header: " + header);
        }

        String diceType = settings.getString();
        int numDice = settings.getInt();
        String mapType = settings.getString();
        long seed = settings.getLong();
        Gdx.app.log(this.getClass().getSimpleName(), "Recieved settings: \n" +
                "diceType: " + diceType + "\n" +
                "numDice: " + numDice + "\n" +
                "mapType: " + mapType + "\n" +
                "seed: " + seed + "\n"
        );

        rng.setFromString(diceType);
        this.maxUnits = numDice;
        rng.setSeed(seed);
        // TODO remove magic Strings
        if (mapType.equals("HexMap")) {
            this.map = new HexMap(7, 7, players);
        } else if (mapType.equals("SquareMap")) {
            this.map = new SquareMap(9, 16, players);
        } else {
            Gdx.app.error(this.getClass().getSimpleName(), "Incorrect or missing mapType: " + mapType);
        }
    }

    public void sendSettings(){
        Gdx.app.log(this.getClass().getSimpleName(), "Sending settings");
        Message message = new Message(new byte[512],"",0);
        // message header
        String header = "mapSettings";
        message.putString(header);
        // actual settings
        String diceType = pref.getDiceType();
        message.putString(diceType);
        int numDice = pref.getNumDice();
        message.putInt(numDice);
        String mapType = pref.getMapType();
        message.putString(mapType);
        long seed = rng.getSeed();
        message.putLong(seed);
        Gdx.app.log(this.getClass().getSimpleName(), "Sent settings: \n" +
                "diceType: " + diceType + "\n" +
                "numDice: " + numDice + "\n" +
                "mapType: " + mapType + "\n" +
                "seed: " + seed + "\n"
        );
        // apply settings and send
        // TODO this doesn't work, for some reason
        Message message2 = new Message(message.getData(), "who cares", 69);
        applySettings(message2, true);
        sender.sendToAllReliably(message.getData());
    }

    @Override
    public void handleDataReceived(Message message) {
        String contents = message.getString();

        //the start-message is redundant if the game is initiated from the quick-game option,
        //the game is already in game-mode when it recieves the start-message.
        if(contents.equals("START")){
            return;
        }

        if(contents.equals("ORDER")) {
            System.out.println("THis unit recieves an order");
            long shuffleSeed = message.getLong();
            System.out.println("ORDER recieved in gameModel" + shuffleSeed);
            Collections.shuffle(players, new Random(shuffleSeed));

            System.out.println("New player-list based on order recieved: " + players.toString());


            //this.setMap("");

            if(this.localPlayer.playerId.equals(players.get(0).playerId)){
                System.out.println("THis unit believes its not host, but settingshost and sends out the settings");
                sendSettings();
            }

        }
        if(contents.equals("mapSettings")){
            System.out.println("This unit recieves mapSettings");
            applySettings(message, false);
        }
    }

    @Override
    public void setSender(IPlayServices playServices) {
        this.sender = playServices;
    }

    public int getNumDice(){
        //return pref.getNumDice();
        return 8;
    }

}
