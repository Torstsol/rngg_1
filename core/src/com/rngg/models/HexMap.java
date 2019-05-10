package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.rngg.game.Rngg;
import com.rngg.utils.RNG;
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
    private int maxUnits;

    public HexMap(int rows, int cols, ArrayList<Player> players, int maxUnits, boolean randomPlayers, JsonValue zoneJSON, int offset) {
        super();

        Gdx.app.log(this.getClass().getSimpleName(),
                "Creating HexMap" + "[" + rows + "][" + cols + "]"
        );

        // The size is the length from a corner to the middle
        this.size = (float) (Rngg.HEIGHT / rows) - (float) (Rngg.HEIGHT / (rows * 3));

        this.shapetype = ShapeRenderer.ShapeType.Filled;
        this.zoneWidth = (float) Math.sqrt(3) * size;
        this.zoneHeight = (float) 2 * size;
        this.rows = rows;
        this.cols = cols;
        this.offset = offset;

        // Need tp extend the array to support offset of negative indexes
        this.zones = new HexZone[rows][cols + offset + 1];
        this.maxUnits = maxUnits;
        this.initializeZones(players, randomPlayers, decodeZoneJSON(zoneJSON));
    }

    public HexMap(int rows, int cols, ArrayList<Player> players, int maxUnits) {
        this(rows, cols, players, maxUnits, true, null, 0);
    }


    /*
     *   The ZoneFormat return value is set to be List<List<int[]>>
     *   This is because the JSON-files for this map type fits well as a 3 dimensional list
     *   The inner list is primitive int[] because the JsonValue only deals in primitives
     */
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
        // Values are offset a bit to fit with the rendering of the map
        // The rendering is offset to not draw the map exactly at coordinate (0,0)
        double q = (Math.sqrt(3) / 3 * (coords.x - size / 2) - 1. / 3 * (coords.y - size)) / size;
        double r = (2. / 3 * (coords.y - size)) / size;

        // The q (column) and r (row) coordinates must be rounded to match their corresponding integer values
        Hex hex = Hex.hexRound(new Hex(q, r));
        return getZone((int) hex.getR(), (int) hex.getQ());
    }

    private void initializeZones(ArrayList<Player> players, boolean randomPlayers, List<List<int[]>> customZones) {
        HashMap<HexZone, ArrayList<HexZone>> zones = new HashMap<HexZone, ArrayList<HexZone>>();

        // When there's no custom map, the default rendered map is a hexagonal HexMap
        if (customZones == null) {
            // indexes are offset to match 2D array storage
            // ref: https://www.redblobgames.com/grids/hexagons/#map-storage
            int startIndex = rows / 2;
            int endCut = 0;
            for (int row = 0; row < rows; row++) {
                for (int col = startIndex; col < rows; col++) {
                    HexZone zone = new HexZone(RNG.choice(players), maxUnits, row, col);
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
                            zone = new HexZone(RNG.choice(players), maxUnits, rowNum, col);
                        } else {
                            zone = new HexZone(players.get(playerNum), maxUnits, rowNum, col);
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
        if (row > rows - 1 || col > cols + this.offset || row < 0 || col + this.offset < 0)
            return null;

        return this.zones[row][col + this.offset];
    }

    private ArrayList<HexZone> generateNeighbors(HexZone zone) {
        ArrayList<HexZone> ret = new ArrayList<HexZone>();

        if (zone == null) return ret;

        int row = zone.getRow();
        int col = zone.getCol();

        // Generated according to ref: https://www.redblobgames.com/grids/hexagons/#neighbors-axial

        HexZone neighbor;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == j) continue;

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
}
