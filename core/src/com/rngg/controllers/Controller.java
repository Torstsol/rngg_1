package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

abstract public class Controller {

    Game game;
    Controller controller;
    protected OrthographicCamera camera;

    public Controller(Game game) {
        this.game = game;
    }

    abstract public void update(float delta);

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

}
