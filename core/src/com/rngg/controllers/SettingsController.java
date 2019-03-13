package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

    public void setInputProcessor(Stage stage) {
        Gdx.input.setInputProcessor(stage);
    }

    public void addActorListeners(final TextButton colorSettingsButton, final TextButton menuButton) {
        colorSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setColorScreen();
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
