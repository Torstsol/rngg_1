package com.rngg.models;

import com.rngg.utils.RNG;

public class SquareZone extends Zone {

    private int row, col;

    public SquareZone(Player player,  int maxUnits, int row, int col) {
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
        return "SquareZone{" +
                "row=" + row +
                ", col=" + col +
                ", player=" + player +
                '}';
    }
}
