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
    private int offset;

    public HexMap(int rows, int cols, Player[] players, boolean randomPlayers, JsonValue zoneJSON, int offset) {
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
        this.offset = offset;

        this.zones = new HexZone[rows][cols + offset + 1];
        this.initializeZones(players, randomPlayers, decodeZoneJSON(zoneJSON));
    }

    public HexMap(int rows, int cols, Player[] players) {
        this(rows, cols, players, true, null, 0);
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
        return getZone((int) hex.getR(), (int) hex.getQ());
    }

    private void initializeZones(Player[] players, boolean randomPlayers, List<List<int[]>> customZones) {
        HashMap<HexZone, ArrayList<HexZone>> zones = new HashMap<HexZone, ArrayList<HexZone>>();

        if (customZones == null) {
            int startIndex = rows / 2;
            int endCut = 0;
            for (int row = 0; row < rows; row++) {
                for (int col = startIndex; col < rows; col++) {
                    HexZone zone = new HexZone(players[(int) (Math.random() * (players.length))], row, col);
                    this.zones[row][col] = zone;
                    Gdx.app.log(this.getClass().getSimpleName(), "generated: " + zone.toString());

                    if (col >= rows - endCut) break;
                }

                if (startIndex > 0) {
                    startIndex--;
                }

                if (startIndex == 0) {
                    endCut++;
                }
            }

            for (HexZone[] row : this.zones) {
                for (HexZone zone : row) {
                    zones.put(zone, generateNeighbors(zone));
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

                        this.zones[rowNum][col + this.offset] = zone;
                        Gdx.app.log(this.getClass().getSimpleName(), "generated: " + zone.toString());
                    }

                    rowNum++;
                }

                playerNum++;
            }

            for (HexZone[] row : this.zones) {
                for (HexZone zone : row) {
                    zones.put(zone, generateNeighbors(zone));
                }
            }
        }

        this.neighbors = zones;
    }

    private HexZone getZone(int row, int col) {
        if (row > rows - 1 || col > cols + this.offset || row < 0 || col + this.offset < 0) return null;

        return this.zones[row][col + this.offset];
    }

    private ArrayList<HexZone> generateNeighbors(HexZone zone) {
        ArrayList<HexZone> ret = new ArrayList<HexZone>();

        if (zone == null) return ret;

        int row = zone.getRow();
        int col = zone.getCol();

        HexZone neighbor;
        for (int i = 0; i < 2; i++) {
            for (int j = -1; j < 1; j++) {
                neighbor = getZone(row + i, col + j);

                if (neighbor != null) ret.add(neighbor);
            }
        }

        return ret;
    }

    public float getSize() {
        return size;
    }

    public float getZoneWidth() {
        return zoneWidth;
    }

    public float getZoneHeight() {
        return zoneHeight;
    }

}
