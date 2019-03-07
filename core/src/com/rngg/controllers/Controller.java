package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;

abstract public class Controller {

    Game game;
    Controller controller;

    public Controller(Game game) {
        this.game = game;
    }

    abstract public void update(float delta);

}
