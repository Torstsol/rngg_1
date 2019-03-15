package com.rngg.models;

import com.badlogic.gdx.graphics.Color;
import com.rngg.views.GameView;

public class SquareZone extends Zone {

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
