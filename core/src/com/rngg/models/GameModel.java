package com.rngg.models;

import com.rngg.views.GameView;

public class GameModel {

    public int playerScore = 0;
    private SquareMap map;

    public GameModel() {
        this.map = new SquareMap(2, 2);
    }

    public void draw(GameView view) {
        map.draw(view);
    }

    public SquareMap getMap() {
        return this.map;
    }

}
