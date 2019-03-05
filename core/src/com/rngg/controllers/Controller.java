package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.rngg.views.GameView;

public class Controller {
    Game game;

    public Controller(Game game) {
        this.game = game;
    }

    public void setView() {
        game.setScreen(new GameView());
    }
}
