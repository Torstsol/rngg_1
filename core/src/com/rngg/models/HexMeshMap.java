package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.rngg.game.Rngg;
import com.rngg.utils.RNG;
import com.rngg.utils.hex.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class HexMeshMap extends GameMap<HexMeshZone, List<List<List<int[]>>>> {
    private float zoneWidth, zoneHeight;

    private float size;
    private int rows, cols, offset, minZoneSize, maxZoneSize, numZones, maxUnits;
    private HexZone[][] zones;
    private List<HexMeshZone> superZones;

    public HexMeshMap(int rows, ArrayList<Player> players, boolean randomPlayers, JsonValue zoneJSON, int offset, int numZones, int maxUnits) {
        super();

        // The size is the length from a corner to the middle
        this.size = (float) (Rngg.HEIGHT / rows) - (float) (Rngg.HEIGHT / (rows * 3));

        this.shapetype = ShapeRenderer.ShapeType.Filled;
        this.zoneWidth = (float) Math.sqrt(3) * size;
        this.zoneHeight = (float) 2 * size;
        this.rows = rows;
        this.cols = (int) (rows * 1.4);
        this.offset = zoneJSON == null ? rows / 2 : offset;
        this.numZones = numZones;
        this.maxZoneSize = (int) (cols / 1.5);
        this.minZoneSize = cols / 5;
        this.maxUnits = maxUnits;

        // Need tp extend the array to support offset of negative indexes
        this.zones = new HexZone[rows][cols + offset + 1];
        this.superZones = new ArrayList<HexMeshZone>();
        this.initializeZones(players, randomPlayers, decodeZoneJSON(zoneJSON));

        Gdx.app.log(this.getClass().getSimpleName(),
                "Creating HexMeshMap" + "[" + rows + "][" + cols + "]"
        );
    }

    public HexMeshMap(int rows, ArrayList<Player> players, boolean randomPlayers, JsonValue zoneJSON, int offset, int maxUnits) {
        this(rows, players, randomPlayers, zoneJSON, offset, rows * 2, maxUnits);
    }

    public HexMeshMap(int rows, ArrayList<Player> players, int maxUnits) {
        this(rows, players, true, null, 0, rows * 2, maxUnits);
    }

    @Override
    public List<List<List<int[]>>> decodeZoneJSON(JsonValue zones) {
        if (zones == null) return null;

        List<List<List<int[]>>> playersWithZones = new ArrayList<List<List<int[]>>>();

        for (JsonValue player : zones) {
            List<List<int[]>> playerZones = new ArrayList<List<int[]>>();

            for (JsonValue subZones : player) {
                List<int[]> playerSubZones = new ArrayList<int[]>();

                for (JsonValue subZone : subZones) {
                    playerSubZones.add(subZone.asIntArray());
                }

                playerZones.add(playerSubZones);
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

    private HexZone addZone(int row, int col, int zoneCount, Player player) {
        if (getZone(row, col) == null) {
            HexZone zone = new HexZone(player, maxUnits, row, col);

            this.zones[row][col + this.offset] = zone;
            this.superZones.get(zoneCount).addSubZone(zone);
            zone.setSuperZone(this.superZones.get(zoneCount));

            return zone;
        }

        return null;
    }

    private HexMeshZone getRandomZone() {
        if (this.superZones.size() == 0) return null;
        return this.superZones.get(RNG.nextInt(0, this.superZones.size()));
    }

    private Integer[] getRandomRowAndCol() {
        Integer[] ret = new Integer[2];

        HexZone zone = null;
        while (zone == null) {
            if (this.superZones.size() == 0) return null;

            HexMeshZone randZone = getRandomZone();
            if (randZone != null) {
                HexZone randSubZone = randZone.getRandomSubZone();

                if (randSubZone == null) continue;

                int row = randSubZone.getRow();
                int col = randSubZone.getCol();
                if (row < rows / 10 || row > rows - rows / 10 || col + this.offset < cols / 10 || col + this.offset > cols - cols / 10) continue;
                zone = randSubZone;
            }
        }

        int i = 0;
        int j = 0;
        int row = zone.getRow();
        int col = zone.getCol();

        int count = 0;
        while (i == j || getZone(row, col) != null) {
            if (count == 6) return null;

            i = RNG.nextInt(-1, 2);
            j = RNG.nextInt(-1, 2);

            row += i;
            col += j;

            count++;
        }

        col += this.offset;

        if (row < rows - rows / 10 && col < cols + this.offset - cols / 10 && row > rows / 10 && col - this.offset > cols / 10) {
            ret[0] = row;
            ret[1] = col;
        } else {
            return null;
        }

        return ret;
    }

    private void initializeZones(ArrayList<Player> players, boolean randomPlayers, List<List<List<int[]>>> customZones) {
        HashMap<HexMeshZone, ArrayList<HexMeshZone>> zones = new HashMap<HexMeshZone, ArrayList<HexMeshZone>>();

        if (customZones == null) {
            // indexes are offset to match 2D array storage
            // ref: https://www.redblobgames.com/grids/hexagons/#map-storage

            Integer lastRow = null;
            Integer lastCol = null;
            int zoneCount = 0;
            int numZones = this.numZones;
            for (int i = 0; i < numZones; i++) {
                Player player = RNG.choice(players);

                int centerRow = (this.superZones.size() == 0 || lastRow == null) ? RNG.nextInt(rows / 4, rows - rows / 4) : lastRow;
                int centerCol = (this.superZones.size() == 0 || lastCol == null) ? RNG.nextInt(cols / 2, cols - cols / 4) : lastCol;

                centerCol -= this.offset;

                this.superZones.add(new HexMeshZone(player, maxUnits));

                int zoneSize = RNG.nextInt(minZoneSize, maxZoneSize);
                for (int num = 0; num < zoneSize; num++) {
                    int row = RNG.nextInt(-1, 2);
                    int col = RNG.nextInt(-1, 2);

                    int count = 0;
                    while (row == col || centerRow + row < 0 || centerRow + row > rows - 1 ||
                            centerCol + col + this.offset < 0 || centerCol + col > cols - this.offset ||
                            getZone(centerRow + row, centerCol + col) != null
                    ) {
                        row = RNG.nextInt(-1, 2);
                        col = RNG.nextInt(-1, 2);
                        count++;

                        if (count == 6) break;
                    }

                    if (count == 6) break;

                    centerCol += col;
                    centerRow += row;

                    addZone(centerRow, centerCol, zoneCount, player);
                }

                Integer[] rowAndCol = getRandomRowAndCol();
                while (rowAndCol == null) {
                    rowAndCol = getRandomRowAndCol();
                }

                lastRow = rowAndCol[0];
                lastCol = rowAndCol[1];

                HexMeshZone currentZone = this.superZones.get(zoneCount);
                if (currentZone.getSubZones().size() < minZoneSize || (this.superZones.size() > 1 && generateNeighbors(currentZone, true) == null)) {
                    this.superZones.remove(currentZone);
                    if (numZones < rows * cols) numZones++;
                    continue;
                }

                Gdx.app.log(this.getClass().getSimpleName(), "generated: " + superZones.get(zoneCount).toString());
                zoneCount++;
            }

            for (HexMeshZone zone : this.superZones) {
                zones.put(zone, generateNeighbors(zone, false));
            }
        } else {
            int playerNum = 0;
            int zoneCount = 0;
            for (List<List<int[]>> playerZones : customZones) {
                for (List<int[]> subZones : playerZones) {
                    Player player = randomPlayers ? RNG.choice(players) : players.get(playerNum);
                    this.superZones.add(new HexMeshZone(player, maxUnits));

                    for (int[] rowCol : subZones) {
                        addZone(rowCol[0], rowCol[1], zoneCount, player);
                    }

                    zoneCount++;
                }

                playerNum++;
            }

            for (HexMeshZone zone : this.superZones) {
                zones.put(zone, generateNeighbors(zone, false));
            }
        }

        this.neighbors = zones;
    }

    private HexZone getZone(int row, int col) {
        if (row > rows - 1 || col + this.offset > cols || row < 0 || col + this.offset < 0) return null;

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

    private ArrayList<HexMeshZone> generateNeighbors(HexMeshZone zone, boolean noSubZoneNeighbors) {
        ArrayList<HexMeshZone> ret = new ArrayList<HexMeshZone>();

        if (zone == null) return null;

        for (HexZone subZone : zone) {
            if (subZone == null) continue;

            int row = subZone.getRow();
            int col = subZone.getCol();

            // Generated according to ref: https://www.redblobgames.com/grids/hexagons/#neighbors-axial

            HexMeshZone neighbor;
            HexZone subZoneNeigbor;
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == j) continue;

                    subZoneNeigbor = getZone(row + i, col + j);
                    neighbor = getSuperZone(subZoneNeigbor);

                    if (subZoneNeigbor != null && !noSubZoneNeighbors) subZone.addNeighbor(subZoneNeigbor);
                    if (neighbor != null && neighbor != zone) ret.add(neighbor);
                }
            }
        }

        if (ret.size() == 0) return null;

        return ret;
    }

    public float getSize() {
        return size;
    }

    public float getZoneWidth() {
        return zoneWidth;
    }
}
