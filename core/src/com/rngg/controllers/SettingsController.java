package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.game.Rngg;

public class SettingsController extends Controller {

    public SettingsController(Rngg game) {
        super(game);
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.B))
            game.screenManager.setMenuScreen();
        else if(Gdx.input.isKeyPressed(Input.Keys.C))
            game.screenManager.setColorScreen();

    }

}
