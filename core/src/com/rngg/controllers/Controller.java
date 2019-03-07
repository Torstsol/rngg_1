package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.rngg.views.GameView;

abstract public class Controller {
    Game game;

    public Controller(Game game) {
        this.game = game;
    }

    abstract public void update(float delta);

    abstract public void handleInput(Input.Keys key);
}
