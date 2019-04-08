package com.rngg.models;

public class HexZone extends Zone {

    private int row, col;

    public HexZone(Player player, int row, int col) {
        this.row = row;
        this.col = col;
        this.player = player;
        this.units = (int) ((Math.random() * 7) + 1);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "HexZone{" +
                "row=" + row +
                ", col=" + col +
                ", player=" + player +
                '}';
    }
}
