package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.configuration.ColorPrefs;
import com.rngg.game.Rngg;
import com.rngg.views.SettingsView;


public class ColorController extends Controller {

    private ColorPrefs pref;

    public ColorController(Rngg game) {
        super(game);

        pref = new ColorPrefs();
        if(!pref.hasColors()){
            pref.setTrueColors();
        }
    }

    @Override
    public void update(float delta) {

        if(Gdx.input.isKeyPressed(Input.Keys.B)) {
            game.setScreen(new SettingsView(game.assetManager, new SettingsController(game)));
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.N)) {
            Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
            pref.setColors("213,0,0", "240,228,66", "0,114,178", "86,180,233");
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
            pref.setColors("118,105,25", "251,223,68", "83,106,171", "152,167,225");
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
            pref.setColors("133,100,0", "255,217,168", "60,109,178", "145,167,237");
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
            pref.setColors("210,20,1", "255,212,228", "1,120,128", "69,185,200");
        }
    }

}
