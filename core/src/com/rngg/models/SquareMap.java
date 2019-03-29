package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.rngg.game.Rngg;
import com.rngg.utils.RNG;

import java.util.ArrayList;
import java.util.HashMap;

public class SquareMap extends GameMap<SquareZone> {

    private int zoneWidth, zoneHeight, rows, cols;
    private SquareZone[][] zones;

    public SquareMap(int rows, int cols, Player[] players) {
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

        HashMap<SquareZone, ArrayList<SquareZone>> zones = new HashMap<SquareZone, ArrayList<SquareZone>>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // TODO look at another way to distribute zones
                SquareZone zone = new SquareZone(players[RNG.nextInt(players.length)], row, col);
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
        this.neighbors = zones;
    }

    private SquareZone getZone(int row, int col) {
        return this.zones[row][col];
    }

    private ArrayList<SquareZone> generateNeighbors(SquareZone zone) {
        ArrayList<SquareZone> ret = new ArrayList<SquareZone>();

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
