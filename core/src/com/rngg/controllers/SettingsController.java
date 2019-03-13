package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.game.Rngg;
import com.rngg.views.ColorView;
import com.rngg.views.MenuView;

public class SettingsController extends Controller {

    public SettingsController(Rngg game) {
        super(game);
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.B))
            game.setScreen(new MenuView(game.assetManager, new MenuController(game)));
        else if(Gdx.input.isKeyPressed(Input.Keys.C))
            game.setScreen(new ColorView(game.assetManager, new ColorController(game)));

    }

}
