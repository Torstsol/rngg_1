package com.rngg.models;

import com.badlogic.gdx.graphics.Color;
import com.rngg.views.GameView;

public class SquareZone extends Zone {

    private int row, col, zoneWidth, zoneHeight;
    private Color color;
    private boolean clicked = false;

    public SquareZone(int row, int col, int zoneWidth, int zoneHeight) {
        this.row = row;
        this.col = col;
        this.zoneWidth = zoneWidth;
        this.zoneHeight = zoneHeight;
        this.color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);

    }

    @Override
    public void draw(GameView view) {
        view.getSR().setColor((this.clicked) ? Color.BLACK : this.color);
        view.getSR().rect(this.col * this.zoneWidth, this.row * this.zoneHeight, this.zoneWidth, this.zoneHeight);
    }

    @Override
    public void drawText(GameView view) {
        view.getFont().draw(view.getBatch(), Integer.toString(this.units),
                (this.col + 0.25f) * this.zoneWidth, (this.row + 0.5f) * this.zoneHeight);
    }

    public void click() {
        this.units++;
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
