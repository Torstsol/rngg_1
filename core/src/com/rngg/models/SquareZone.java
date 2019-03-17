package com.rngg.models;

public class SquareZone extends Zone {

    private int row, col;
    private boolean clicked = false;

    public SquareZone(Player player, int row, int col) {
        this.row = row;
        this.col = col;
        this.player = player;
        this.units = (int) ((Math.random() * 7) + 1);
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

    public boolean getClicked() {
        return this.clicked;
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
