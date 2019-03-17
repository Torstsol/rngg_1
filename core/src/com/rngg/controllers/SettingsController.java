package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rngg.configuration.ColorPrefs;
import com.rngg.game.Rngg;

public class SettingsController extends Controller {

    private ColorPrefs pref;

    public SettingsController(Rngg game) {
        super(game);

        pref = new ColorPrefs();
        if(!pref.hasColors()){
            pref.setTrueColors();
        }
    }

    @Override
    public void update(float delta) {}

    public void setInputProcessor(Stage stage) {
        Gdx.input.setInputProcessor(stage);
    }

    public void addActorListeners(final TextButton tcSettingsButton,
                                  final TextButton protSettingsButton,
                                  final TextButton deutSettingsButton,
                                  final TextButton tritSettingsButton,
                                  final TextButton menuButton) {
        tcSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
                pref.setColors("213,0,0", "240,228,66", "0,114,178", "86,180,233");
            }
        });

        protSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
                pref.setColors("118,105,25", "251,223,68", "83,106,171", "152,167,225");
            }
        });

        deutSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
                pref.setColors("133,100,0", "255,217,168", "60,109,178", "145,167,237");
            }
        });

        tritSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
                pref.setColors("210,20,1", "255,212,228", "1,120,128", "69,185,200");
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
