package com.rngg.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SquareZone extends Zone {

    private int row, col, zoneWidth, zoneHeight;
    private Color color;

    public SquareZone(int row, int col, int zoneWidth, int zoneHeight) {
        this.row = row;
        this.col = col;
        this.zoneWidth = zoneWidth;
        this.zoneHeight = zoneHeight;
        this.color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);

    }


    @Override
    public void draw(ShapeRenderer sr) {
        sr.setColor(this.color);
        sr.rect(this.col*this.zoneWidth, this.row*this.zoneHeight, this.zoneWidth, this.zoneHeight);
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
