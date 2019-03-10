package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.rngg.game.Rngg;

import java.util.ArrayList;
import java.util.HashMap;

public class SquareMap extends GameMap<SquareZone> {

    private int zoneWidth, zoneHeight, rows, cols;
    private SquareZone[][] zones;

    public SquareMap(int rows, int cols){
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

        for(int row = 0; row<rows; row++){
            for(int col = 0; col<cols; col++) {
                SquareZone zone = new SquareZone(col, row, zoneWidth, zoneHeight);
                this.zones[row][col] = zone;
                Gdx.app.log(this.getClass().getSimpleName(),"generated: " + zone.toString());
            }
        }
        for(int row = 0; row<rows; row++){
            for(int col = 0; col<cols; col++) {
                SquareZone zone = getZone(col, row);
                zones.put(zone, generateNeighbors(zone));
            }
        }
        this.neighbors = zones;
    }

    private SquareZone getZone(int x, int y) {
        return this.zones[y][x];
    }

    private ArrayList<SquareZone> generateNeighbors(SquareZone zone) {
        ArrayList<SquareZone> ret = new ArrayList<SquareZone>();

        int x = zone.getX();
        int y = zone.getY();

        if(x-1 >= 0) {
            ret.add(getZone(x-1, y));
        }
        if(x+1 < cols) {
            ret.add(getZone(x+1, y));
        }

        if(y-1 >= 0) {
            ret.add(getZone(x, y-1));
        }
        if(y+1 < rows) {
            ret.add(getZone(x, y+1));
        }

        return ret;
    }

    @Override
    public SquareZone screenCoordToZone(Vector2 coords) {
        return getZone((int) coords.x/this.zoneWidth,(int) coords.y/zoneHeight);
    }
}
