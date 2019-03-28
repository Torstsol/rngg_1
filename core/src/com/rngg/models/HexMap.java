package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.rngg.game.Rngg;
import com.rngg.utils.hex.Hex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HexMap extends GameMap<HexZone, List<List<int[]>>> {
    private float zoneWidth, zoneHeight;

    private float size;
    private int rows, cols;
    private HexZone[][] zones;

    public HexMap(int rows, int cols, Player[] players, boolean randomPlayers, JsonValue zoneJSON) {
        super();

        Gdx.app.log(this.getClass().getSimpleName(),
                "Creating HexMap" + "[" + rows + "][" + cols + "]"
        );

        this.size = (float) (Rngg.HEIGHT / rows) - (float) (Rngg.HEIGHT / (rows * 3));

        this.shapetype = ShapeRenderer.ShapeType.Filled;
        this.zoneWidth = (float) Math.sqrt(3) * size;
        this.zoneHeight = (float) 2 * size;
        this.rows = rows;
        this.cols = cols;

        this.zones = new HexZone[rows][cols];
        this.initializeZones(players, randomPlayers, decodeZoneJSON(zoneJSON));
    }

    public float getSize() {
        return size;
    }

    @Override
    public List<List<int[]>> decodeZoneJSON(JsonValue zones) {
        if (zones == null) return null;

        List<List<int[]>> playersWithZones = new ArrayList<List<int[]>>();

        for (JsonValue player : zones) {
            List<int[]> playerZones = new ArrayList<int[]>();

            for (JsonValue row : player) {
                playerZones.add(row.asIntArray());
            }

            playersWithZones.add(playerZones);
        }

        return playersWithZones;
    }

    @Override
    public HexZone _screenCoordToZone(Vector2 coords) {
        double q = (Math.sqrt(3)/3 * (coords.x - size / 2)  -  1./3 * coords.y) / size;
        double r =  (2./3 * coords.y) / size;

        Hex hex = Hex.hexRound(new Hex(q, r));
        return getZone((int) hex.getR(), (int) hex.getQ() + (int) hex.getR() / 2);
    }

    private void initializeZones(Player[] players, boolean randomPlayers, List<List<int[]>> customZones) {
        HashMap<HexZone, ArrayList<HexZone>> zones = new HashMap<HexZone, ArrayList<HexZone>>();

        int skew = 0; // Needed for axial coord conversion
        int loopCount = 0;
        if (customZones == null) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    HexZone zone = new HexZone(players[(int) (Math.random() * (players.length))], row, col + skew);
                    this.zones[row][col] = zone;
                    Gdx.app.log(this.getClass().getSimpleName(), "generated: " + zone.toString());
                }

                if (loopCount == 1) {
                    loopCount = 0;
                    skew--;
                } else {
                    loopCount++;
                }
            }

            loopCount = 0;
            skew = 0;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    HexZone zone = getZone(row, col);
                    zones.put(zone, generateNeighbors(zone, skew));
                }
                if (loopCount == 1) {
                    loopCount = 0;
                    skew--;
                } else {
                    loopCount++;
                }
            }

        } else {
            int playerNum = 0;
            for (List<int[]> playerZones : customZones) {
                int rowNum = 0;
                for (int[] row : playerZones) {
                    for (int col : row) {

                        HexZone zone;
                        if (randomPlayers) {
                            zone = new HexZone(players[(int) (Math.random() * (players.length))], rowNum, col);
                        } else {
                            zone = new HexZone(players[playerNum], rowNum, col);
                        }

                        this.zones[rowNum][col] = zone;
                        Gdx.app.log(this.getClass().getSimpleName(), "generated: " + zone.toString());
                    }

                    rowNum++;
                }
                playerNum++;
            }

            for (List<int[]> playerZones : customZones) {
                int rowNum = 0;
                for (int[] row : playerZones) {
                    for (int col : row) {
                        HexZone zone = getZone(rowNum, col);
                        zones.put(zone, generateNeighbors(zone, 0));
                    }

                    rowNum++;
                }
                playerNum++;
            }
        }

        this.neighbors = zones;
    }

    public HexMap(int rows, int cols, Player[] players) {
        this(rows, cols, players, true, null);
    }

    private HexZone getZone(int row, int col) {
        if (row > rows - 1 || col > cols - 1 || row < 0 || col < 0) return null;

        return this.zones[row][col];
    }

    private ArrayList<HexZone> generateNeighbors(HexZone zone, int skew) {
        ArrayList<HexZone> ret = new ArrayList<HexZone>();

        if (zone == null) return ret;

        int row = zone.getRow();
        int col = zone.getCol() + skew * -1;

        HexZone neighbor;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                neighbor = getZone(row + i, col + j);

                if (neighbor != null) ret.add(neighbor);
            }
        }

        return ret;
    }

    public float getZoneWidth() {
        return zoneWidth;
    }

    public float getZoneHeight() {
        return zoneHeight;
    }
}
