package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.rngg.views.SettingsView;


public class ColorController extends Controller {

    Preferences prefs;

    public ColorController(Game game) {
        super(game);

        prefs = Gdx.app.getPreferences("color-preferences");
        this.putColors("213,0,0", "240,228,66", "0,114,178", "86,180,233");
        prefs.flush();
    }

    @Override
    public void update(float delta) {
        //Gdx.app.log(this.getClass().getSimpleName(), "update");

        if(Gdx.input.isKeyPressed(Input.Keys.B)) {
            game.setScreen(new SettingsView(new SettingsController(game)));
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.N)) {
            Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
            this.putColors("213,0,0", "240,228,66", "0,114,178", "86,180,233");
            prefs.flush();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
            this.putColors("118,105,25", "251,223,68", "83,106,171", "152,167,225");
            prefs.flush();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
            this.putColors("133,100,0", "255,217,168", "60,109,178", "145,167,237");
            prefs.flush();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
            this.putColors("210,20,1", "255,212,228", "1,120,128", "69,185,200");
            prefs.flush();
        }
    }

    private void putColors(String r, String y, String b, String sb){
        prefs.putString("red", r);
        prefs.putString("yellow", y);
        prefs.putString("blue", b);
        prefs.putString("sky blue", sb);
    }

}
