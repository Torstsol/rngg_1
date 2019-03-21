package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rngg.configuration.GamePreferences;
import com.rngg.game.Rngg;

public class SettingsController extends Controller {

    private GamePreferences pref;

    public SettingsController(Rngg game) {
        super(game);

        pref = new GamePreferences();
        if(!pref.hasColors()){
            pref.setTrueColors();
        }
    }

    @Override
    public void update(float delta) {}

    public void addActorListeners(final TextButton ncSettingsButton,
                                  final TextButton cbSettingsButton,
                                  final TextButton menuButton) {

        ncSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
                pref.setColors("255,0,0", "0,255,0", "0,0,255", "255,255,0");
            }
        });

        cbSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
                pref.setColors("213,0,0", "240,228,66", "0,114,178", "86,180,233");
            }
        });

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setMenuScreen();
            }
        });
    }

}
