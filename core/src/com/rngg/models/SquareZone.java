package com.rngg.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SquareZone extends Zone {

    private int x, y, zoneWidth, zoneHeight;
    private Color color;

    public SquareZone(int x, int y, int zoneWidth, int zoneHeight) {
        this.x = x;
        this.y = y;
        this.zoneWidth = zoneWidth;
        this.zoneHeight = zoneHeight;
        this.color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1);

    }


    @Override
    public void draw(ShapeRenderer sr) {
        sr.setColor(this.color);
        sr.rect(this.x*this.zoneWidth, this.y*this.zoneHeight, this.zoneWidth, this.zoneHeight);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "SquareZone{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
