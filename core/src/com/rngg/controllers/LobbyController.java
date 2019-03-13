package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;

public class LobbyController extends Controller {

    public LobbyController(Rngg game) {
        super(game);
    }

    @Override
    public void update(float delta) {}

    public void setInputProcessor(Stage stage) {
        Gdx.input.setInputProcessor(stage);
    }

    public void addActorListeners(final TextButton gameButton, final TextButton menuButton) {
        gameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setGameScreen(new GameModel());
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
