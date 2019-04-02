package com.rngg.models;

import com.rngg.utils.RNG;

public class SquareZone extends Zone {

    private int row, col;

    public SquareZone(Player player, int row, int col) {
        this.row = row;
        this.col = col;
        this.player = player;
        // TODO change unit distribution
        // TODO hardcoded for max 8
        this.units = RNG.nextInt(1, 8);
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
