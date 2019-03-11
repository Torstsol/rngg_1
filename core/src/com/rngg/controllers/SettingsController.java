package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.views.MenuView;

public class SettingsController extends Controller {

    public SettingsController(Game game) {
        super(game);
    }

    @Override
    public void update(float delta) {
        Gdx.app.log(this.getClass().getSimpleName(), "update");

        if(Gdx.input.isKeyPressed(Input.Keys.B))
            game.setScreen(new MenuView(new MenuController(game), game));
    }

}
