package com.rngg.models;

public class HexZone extends Zone {

    private int row, col;

    public HexZone(Player player, int maxUnits, int row, int col) {
        super(player, maxUnits);
        this.row = row;
        this.col = col;
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
                "id=" + id +
                "row=" + row +
                ", col=" + col +
                ", player=" + player +
                '}';
    }
}
