package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.rngg.configuration.GamePreferences;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class TutorialModel {
    private GameMap map;
    private ArrayList<Player> players;
    private int maxUnits = 8;
    private int numPlayers;
    private GamePreferences prefs = GamePreferences.getInstance();
    private int step;
    private List<Integer> zoneIds;
    private List<Zone> zones;

    public TutorialModel(String filename) {
        this.players = new ArrayList<Player>();
        for (int i = 0; i < 4; i++) {
            this.players.add(new Player("BOT" + i, "6969", true, i == 0, prefs.getColorArray().get(i)));
        }

        this.step = 1;
        this.map = loadMap(filename);
        this.zoneIds = new ArrayList<Integer>();
        this.zones = new ArrayList<Zone>();

        // Raw types makes it having to be passed in as Object. It's somewhat quirky
        for (Object zone : map.getZones()) {
            if (zone instanceof Zone) zoneIds.add(((Zone) zone).getId());
        }

        Collections.sort(zoneIds);

        // zoneIds need to be sorted before zones are filled in
        for (int i = 0; i < zoneIds.size(); i++) {
            zones.add(map.getZoneById(zoneIds.get(i)));
        }
    }

    private GameMap loadMap(String fileName) {
        String mapType = "";
        int totalCols = -1;
        int totalRows = -1;
        int maxPlayers = -1;
        boolean randomPlayers = false;
        int offset = 0;
        JsonValue zones = null;
        Deque<Integer> units = null;

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
            } else if (value.name.equals("units")) {
                units = new ArrayDeque<Integer>();

                for (int i : value.asIntArray()) {
                    units.add(i);
                }
            }
        }

        if (!randomPlayers || this.numPlayers > maxPlayers) {
            this.numPlayers = maxPlayers;
            this.players = new ArrayList<Player>(this.players.subList(0, maxPlayers));
        }

        if (mapType.equals("SquareMap")) {
            return new SquareMap(totalRows, totalCols, this.players, this.maxUnits, randomPlayers, zones);
        } else if (mapType.equals("HexMap")) {
            return new HexMap(totalRows, totalCols, this.players, this.maxUnits, randomPlayers, zones, offset);
        } else if (mapType.equals("HexMeshMap")) {
            return new HexMeshMap(totalRows, this.players, randomPlayers, zones, offset, this.maxUnits, units);
        }

        return new SquareMap(9, 16, this.players, this.maxUnits);
    }

    // TODO If not for limited timeframe, hardcoding should be migrated to JSON-files
    public void click(Vector3 coords) {
        Zone clickedZone = map.screenCoordToZone(new Vector2(coords.x, coords.y));

        if (clickedZone == null) return;

        int id = clickedZone.getId();

        switch (step) {
            case 1: {
                if (id == zoneIds.get(1)) nextStep();
                break;
            } case 2: {
                if (id == zoneIds.get(3)) nextStep();
                break;
            } case 3: {
                if (id == zoneIds.get(3)) nextStep();
                break;
            } case 4: {
                if (id == zoneIds.get(2)) nextStep();
                break;
            } case 5: {
                if (id == zoneIds.get(0)) nextStep();
                break;
            } case 6: {
                if (id == zoneIds.get(4)) nextStep();
                break;
            } case 7: {
                if (id == zoneIds.get(4)) nextStep();
                break;
            } case 8: {
                if (id == zoneIds.get(5)) nextStep();
                break;
            }
        }
    }

    // TODO If not for limited timeframe, hardcoding should be migrated to JSON-files
    private void updateStep() {
        if (step == 1 || step == 2) {
            zones.get(0).setUnits(8);
            zones.get(0).setPlayer(players.get(0));
            zones.get(1).setUnits(8);
            zones.get(1).setPlayer(players.get(0));
            zones.get(2).setUnits(2);
            zones.get(2).setPlayer(players.get(1));
            zones.get(3).setUnits(5);
            zones.get(3).setPlayer((players.get(1)));
            zones.get(4).setUnits(2);
            zones.get(4).setPlayer(players.get(2));
            zones.get(5).setUnits(8);
            zones.get(5).setPlayer(players.get(2));

            if (step == 1) zones.get(1).unClick();
            if (step == 2) zones.get(1).click();

        } else if (step == 3 || step == 4) {
            zones.get(0).setUnits(8);
            zones.get(0).setPlayer(players.get(0));
            zones.get(1).setUnits(1);
            zones.get(1).setPlayer(players.get(0));
            zones.get(2).setUnits(2);
            zones.get(2).setPlayer(players.get(1));
            zones.get(3).setUnits(7);
            zones.get(3).setPlayer((players.get(0)));
            zones.get(4).setUnits(2);
            zones.get(4).setPlayer(players.get(2));
            zones.get(5).setUnits(8);
            zones.get(5).setPlayer(players.get(2));

            if (step == 3) {
                zones.get(1).unClick();
                zones.get(3).unClick();
            } else if (step == 4) zones.get(3).click();

        } else if (step == 5 || step == 6) {
            zones.get(0).setUnits(8);
            zones.get(0).setPlayer(players.get(0));
            zones.get(1).setUnits(1);
            zones.get(1).setPlayer(players.get(0));
            zones.get(2).setUnits(6);
            zones.get(2).setPlayer(players.get(0));
            zones.get(3).setUnits(1);
            zones.get(3).setPlayer((players.get(0)));
            zones.get(4).setUnits(2);
            zones.get(4).setPlayer(players.get(2));
            zones.get(5).setUnits(8);
            zones.get(5).setPlayer(players.get(2));

            if (step == 5) {
                zones.get(3).unClick();
                zones.get(0).unClick();
            } else if (step == 6) zones.get(0).click();
        } else if (step == 7 || step == 8) {
            zones.get(0).setUnits(1);
            zones.get(0).setPlayer(players.get(0));
            zones.get(1).setUnits(1);
            zones.get(1).setPlayer(players.get(0));
            zones.get(2).setUnits(6);
            zones.get(2).setPlayer(players.get(0));
            zones.get(3).setUnits(1);
            zones.get(3).setPlayer((players.get(0)));
            zones.get(4).setUnits(7);
            zones.get(4).setPlayer(players.get(0));
            zones.get(5).setUnits(8);
            zones.get(5).setPlayer(players.get(2));

            if (step == 7) {
                zones.get(0).unClick();
                zones.get(4).unClick();
            } else if (step == 8) zones.get(4).click();
        } else if (step == 9) {
            zones.get(0).setUnits(1);
            zones.get(0).setPlayer(players.get(0));
            zones.get(1).setUnits(2);
            zones.get(1).setPlayer(players.get(0));
            zones.get(2).setUnits(7);
            zones.get(2).setPlayer(players.get(0));
            zones.get(3).setUnits(3);
            zones.get(3).setPlayer((players.get(0)));
            zones.get(4).setUnits(2);
            zones.get(4).setPlayer(players.get(0));
            zones.get(5).setUnits(8);
            zones.get(5).setPlayer(players.get(2));

            if (step == 9) zones.get(4).unClick();
        } else if (step == 10) {
            zones.get(0).setUnits(1);
            zones.get(0).setPlayer(players.get(0));
            zones.get(1).setUnits(2);
            zones.get(1).setPlayer(players.get(0));
            zones.get(2).setUnits(7);
            zones.get(2).setPlayer(players.get(0));
            zones.get(3).setUnits(3);
            zones.get(3).setPlayer((players.get(0)));
            zones.get(4).setUnits(7);
            zones.get(4).setPlayer(players.get(2));
            zones.get(5).setUnits(1);
            zones.get(5).setPlayer(players.get(2));
        }
    }

    public void prevStep() {
        if (step > 1) step--;
        updateStep();
    }

    public void nextStep() {
        if (step < 10) step++;
        updateStep();
    }

    public boolean hasPrev() { return step > 1; }

    public boolean hasNext() { return step < 10; }

    public int getStep() {
        return step;
    }

    public GameMap getMap() {
        return map;
    }
}
