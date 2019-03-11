package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.views.LobbyView;
import com.rngg.views.SettingsView;

public class MenuController extends Controller {

    public MenuController(Game game) {
        super(game);
    }

    @Override
    public void update(float delta) {
        Gdx.app.debug(this.getClass().getSimpleName(), "update");

        if(Gdx.input.isKeyPressed(Input.Keys.L))
            game.setScreen(new LobbyView(new LobbyController(game)));
        else if(Gdx.input.isKeyPressed(Input.Keys.S))
            game.setScreen(new SettingsView(new SettingsController(game)));
    }

}
