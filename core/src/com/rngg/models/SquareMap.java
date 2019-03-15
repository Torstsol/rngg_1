package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.rngg.game.Rngg;
import com.rngg.views.GameView;

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
                SquareZone zone = new SquareZone(players[(int) (Math.random() * (players.length))], row, col, zoneWidth, zoneHeight);
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
    public SquareZone screenCoordToZone(Vector2 coords) {
        return getZone((int) coords.y / this.zoneHeight, (int) coords.x / zoneWidth);
    }
}

class SquareZone extends Zone {

    private int row, col, zoneWidth, zoneHeight;
    private boolean clicked = false;

    public SquareZone(Player player, int row, int col, int zoneWidth, int zoneHeight) {
        this.row = row;
        this.col = col;
        this.zoneWidth = zoneWidth;
        this.zoneHeight = zoneHeight;
        this.player = player;
        this.units = (int) ((Math.random() * 7) + 1);
    }

    @Override
    public void draw(GameView view) {
        view.getSR().setColor((this.clicked) ? Color.BLACK : this.player.getColor());
        view.getSR().rect(this.col * this.zoneWidth, this.row * this.zoneHeight, this.zoneWidth, this.zoneHeight);
    }

    @Override
    public void drawText(GameView view) {
        view.getFont().draw(view.getBatch(), Integer.toString(this.units),
                (this.col + 0.25f) * this.zoneWidth, (this.row + 0.5f) * this.zoneHeight);
    }

    public void click() {
        // TODO move this and unClick to Zone?
        this.clicked = true;
    }

    public void unClick() {
        this.clicked = false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "SquareZone{" +
                "row=" + row +
                ", col=" + col +
                ", player=" + player +
                '}';
    }
}
