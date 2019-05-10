package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.rngg.game.Rngg;
import com.rngg.utils.RNG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SquareMap extends GameMap<SquareZone, List<List<int[]>>> {
    private int zoneWidth, zoneHeight, rows, cols, maxUnits;
    private SquareZone[][] zones;

    public SquareMap(int rows, int cols, ArrayList<Player> players, int maxUnits, boolean randomPlayers, JsonValue zoneJSON) {
        super();

        Gdx.app.log(this.getClass().getSimpleName(),
                "Creating SquareMap" + "[" + rows + "][" + cols + "]"
        );

        this.shapetype = ShapeRenderer.ShapeType.Filled;
        this.zoneWidth = Rngg.WIDTH / cols;
        this.zoneHeight = Rngg.HEIGHT / rows;
        this.rows = rows;
        this.cols = cols;
        this.zones = new SquareZone[rows][cols];
        this.maxUnits = maxUnits;
        this.initializeZones(players, randomPlayers, decodeZoneJSON(zoneJSON));
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

    private void initializeZones(ArrayList<Player> players, boolean randomPlayers, List<List<int[]>> customZones) {
        HashMap<SquareZone, ArrayList<SquareZone>> zones = new HashMap<SquareZone, ArrayList<SquareZone>>();
        // no custom level
        if (customZones == null) {
            //
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    // TODO look at another way to distribute zones

                    SquareZone zone = new SquareZone(RNG.choice(players), maxUnits, row, col);
                    this.zones[row][col] = zone;
                    Gdx.app.log(this.getClass().getSimpleName(), "generated: " + zone.toString());
                }
            }

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    SquareZone zone = getZone(row, col);
                    zones.put(zone, generateNeighbors(zone));
                }
            }

        // custom levels from JSON
        } else {
            int playerNum = 0;
            for (List<int[]> playerZones : customZones) {
                int rowNum = 0;
                for (int[] row : playerZones) {
                    for (int col : row) {

                        SquareZone zone;
                        if (randomPlayers) {
                            zone = new SquareZone(RNG.choice(players), maxUnits, rowNum, col);
                        } else {
                            zone = new SquareZone(players.get(playerNum), maxUnits, rowNum, col);
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
                        SquareZone zone = getZone(rowNum, col);
                        zones.put(zone, generateNeighbors(zone));
                    }

                    rowNum++;
                }
                playerNum++;
            }
        }

        this.neighbors = zones;
    }

    public SquareMap(int rows, int cols, ArrayList<Player> players, int maxUnits) {
        this(rows, cols, players, maxUnits, true, null);
    }

    private SquareZone getZone(int row, int col) {
        if (row > rows - 1 || col > cols - 1 || row < 0 || col < 0) return null;

        return this.zones[row][col];
    }

    private ArrayList<SquareZone> generateNeighbors(SquareZone zone) {
        ArrayList<SquareZone> ret = new ArrayList<SquareZone>();

        if (zone == null) return ret;

        int row = zone.getRow();
        int col = zone.getCol();

        if (row - 1 >= 0) {
            ret.add(getZone(row - 1, col));
        }

        if (row + 1 < rows) {
            ret.add(getZone(row + 1, col));
        }

        if (col - 1 >= 0) {
            ret.add(getZone(row, col - 1));
        }

        if (col + 1 < cols) {
            ret.add(getZone(row, col + 1));
        }

        return ret;
    }

    @Override
    public SquareZone _screenCoordToZone(Vector2 coords) {
        return getZone((int) coords.y / this.zoneHeight, (int) coords.x / zoneWidth);
    }

    public int getZoneWidth() {
        return zoneWidth;
    }

    public int getZoneHeight() {
        return zoneHeight;
    }
}
