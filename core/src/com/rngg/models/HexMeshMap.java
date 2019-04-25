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


public class HexMeshMap extends GameMap<HexMeshZone, List<List<int[]>>> {
    private float zoneWidth, zoneHeight;

    private float size;
    private int rows, cols, offset, minZoneSize, maxZoneSize, numZones;
    private HexZone[][] zones;
    private List<HexMeshZone> superZones;

    public HexMeshMap(int rows, int cols, Player[] players, boolean randomPlayers, JsonValue zoneJSON, int offset, int numZones) {
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
        this.offset = zoneJSON == null ? rows / 2 : offset;
        this.numZones = numZones;
        this.maxZoneSize = rows * cols / 4;
        this.minZoneSize = rows * cols / 10;

        // Need tp extend the array to support offset of negative indexes
        this.zones = new HexZone[rows][cols + offset + 1];
        this.superZones = new ArrayList<HexMeshZone>();
        this.initializeZones(players, randomPlayers, decodeZoneJSON(zoneJSON));
    }

    public HexMeshMap(int rows, int cols, Player[] players) {
        this(rows, cols, players, true, null, 0,rows / 2);
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
    public HexMeshZone _screenCoordToZone(Vector2 coords) {
        // Values are offset a bit to fit with the rendering of the map
        // The rendering is offset to not draw the map exactly at coordinate (0,0)
        double q = (Math.sqrt(3)/3 * (coords.x - size / 2)  -  1./3 * (coords.y - size)) / size;
        double r =  (2./3 * (coords.y - size)) / size;

        // The q (column) and r (row) coordinates must be rounded to match their corresponding integer values
        Hex hex = Hex.hexRound(new Hex(q, r));
        return getSuperZone(getZone((int) hex.getR(), (int) hex.getQ()));
    }

    private HexZone addZone(Player[] players, int row, int col, int zoneCount, int player) {
        if (getZone(row, col) == null) {
            HexZone zone = new HexZone(players[player], row, col);

            //System.out.println("Col: " + col + " Col + offset: " + (col + this.offset));

            this.zones[row][col + this.offset] = zone;
            this.superZones.get(zoneCount).addSubZone(zone);
            Gdx.app.log(this.getClass().getSimpleName(), "generated: " + zone.toString());

            return zone;
        }

        return null;
    }

    private void initializeZones(Player[] players, boolean randomPlayers, List<List<int[]>> customZones) {
        HashMap<HexMeshZone, ArrayList<HexMeshZone>> zones = new HashMap<HexMeshZone, ArrayList<HexMeshZone>>();

        if (customZones == null) {
            // indexes are offset to match 2D array storage
            // ref: https://www.redblobgames.com/grids/hexagons/#map-storage

            int lastRow = -1;
            int lastCol = -1;
            int giveUp = 0;
            for (int zoneCount = 0; zoneCount < numZones; zoneCount++) {
                int player = (int) (Math.random() * (players.length));

                int centerRow = (lastRow == -1) ? RNG.nextInt(rows / 4, rows - rows / 4) : lastRow;
                int centerCol = (lastCol == -1) ? RNG.nextInt(rows / 4, cols - cols / 4) : lastCol;

                centerCol -= this.offset;

                this.superZones.add(new HexMeshZone(players[player]));

                if (addZone(players, centerRow, centerCol, zoneCount, player) == null && giveUp < 100) {
                    numZones++;
                    giveUp++;
                    continue;
                }

                giveUp = 0;

                for (int num = 0; num < RNG.nextInt(minZoneSize, maxZoneSize); num++) {
                    int row;
                    int col;

                    int count = 0;
                    do {
                        count++;
                        row = RNG.nextInt(-1, 1);
                        col = RNG.nextInt(-1, 1);

                        if (count == 6) break;
                    } while (row == col || centerRow + row < 0 || centerRow + row > rows - 1 || centerCol + col + this.offset < 0 || centerCol + col > cols - 1 - this.offset || getZone(centerRow + row, centerCol + col) != null);

                    if (count == 6) break;
                    System.out.println("yes");

                    centerCol = centerCol + col;
                    centerRow = centerRow + row;

                    addZone(players, centerRow, centerCol, zoneCount, player);
                }

                boolean breakTrough = false;
                for (HexZone zone : this.superZones.get(zoneCount)) {
                    if (breakTrough) break;

                    for (int i = -1; i < 2; i++) {
                        if (breakTrough) break;

                        for (int j = -1; j < 2; j++) {
                            if (i == j) continue;
                            int row = zone.getRow() + i;
                            int col = zone.getCol() + j;

                            if (row < rows - 1 || col < cols - 1 - this.offset || row > 0 || col + this.offset > 0) {
                                if (getZone(row, col) != null) continue;

                                lastRow = row;
                                lastCol = col + this.offset;

                                breakTrough = true;
                                break;
                            }
                        }
                    }
                }

            }

            System.out.println(superZones);

            for (HexMeshZone zone : this.superZones) {
                zones.put(zone, generateNeighbors(zone));
            }

            /*for (HexZone[] row : this.zones) {
                for (HexZone zone : row) {
                    zones.put(zone, generateNeighbors(zone));
                }
            }*/
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

            for (HexMeshZone zone : this.superZones) {
                zones.put(zone, generateNeighbors(zone));
            }

            /*for (HexZone[] row : this.zones) {
                for (HexZone zone : row) {
                    zones.put(zone, generateNeighbors(zone));
                }
            }*/
        }

        this.neighbors = zones;
    }

    private HexZone getZone(int row, int col) {
        if (row > rows - 1 || col > cols - 1 + this.offset || row < 0 || col + this.offset < 0) return null;

        return this.zones[row][col + this.offset];
    }

    private HexMeshZone getSuperZone(HexZone zone) {
        for (HexMeshZone superZone : this.superZones) {
            for (HexZone subZone : superZone) {
                if (subZone == zone) {
                    return superZone;
                }
            }
        }

        return null;
    }

    private ArrayList<HexMeshZone> generateNeighbors(HexMeshZone zone) {
        ArrayList<HexMeshZone> ret = new ArrayList<HexMeshZone>();

        if (zone == null) return ret;

        for (HexZone subZone : zone) {
            if (subZone == null) continue;

            int row = subZone.getRow();
            int col = subZone.getCol();

            // Generated according to ref: https://www.redblobgames.com/grids/hexagons/#neighbors-axial

            HexMeshZone neighbor;
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == j) continue;

                    neighbor = getSuperZone(getZone(row + i, col + j));

                    if (neighbor != null && neighbor != zone) ret.add(neighbor); // TODO Stygg kjøretid, skriv om!
                }
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
