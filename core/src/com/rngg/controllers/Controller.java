package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.rngg.game.Rngg;

abstract public class Controller {

    protected Controller controller;
    protected OrthographicCamera camera;
    protected Rngg game;

    public Controller(Rngg game) {
        this.game = game;
    }

    abstract public void update(float delta);

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void setInputProcessor(Stage stage) {
        Gdx.input.setInputProcessor(stage);
    }

}
