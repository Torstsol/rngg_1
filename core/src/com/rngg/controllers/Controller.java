package com.rngg.controllers;

import com.rngg.game.Rngg;

abstract public class Controller {

    Rngg game;

    public Controller(Rngg game) {
        this.game = game;
    }

    abstract public void update(float delta);

}
