package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.game.Rngg;

public class MenuController extends Controller {

    public MenuController(Rngg game) {
        super(game);
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.L))
            game.screenManager.setLobbyScreen();
        else if(Gdx.input.isKeyPressed(Input.Keys.S))
            game.screenManager.setSettingsScreen();
    }

}
