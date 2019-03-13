package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.game.Rngg;
import com.rngg.views.LobbyView;
import com.rngg.views.SettingsView;

public class MenuController extends Controller {

    public MenuController(Rngg game) {
        super(game);
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.L))
            game.setScreen(new LobbyView(game.assetManager, new LobbyController(game)));
        else if(Gdx.input.isKeyPressed(Input.Keys.S))
            game.setScreen(new SettingsView(game.assetManager, new SettingsController(game)));
    }

}
