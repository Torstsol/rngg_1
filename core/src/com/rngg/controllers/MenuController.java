package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rngg.game.Rngg;
import com.rngg.models.TutorialModel;

public class MenuController extends Controller {


    public MenuController(Rngg game) {
        super(game);
    }

    @Override
    public void update(float delta) {}

    public void addActorListeners(final TextButton lobbyButton, final TextButton tutorialButton, final TextButton settingsButton, final TextButton exitButton) {
        lobbyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setLobbyScreen();
            }
        });

        tutorialButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setTutorialScreen(new TutorialModel("levels/Tutorial.json"));
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setSettingsScreen();
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

}
