package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rngg.game.Rngg;
import com.rngg.models.TutorialModel;

public class TutorialController extends Controller {
    private final TutorialModel model;
    private InputMultiplexer inputMultiplexer;

    public TutorialController(final Rngg game, final TutorialModel tutorialModel) {
        super(game);

        this.model = tutorialModel;

        InputProcessor gameInputProcessor = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 unprojectedCoords = new Vector3(screenX, screenY, 0);
                camera.unproject(unprojectedCoords);

                TutorialController.this.model.click(unprojectedCoords);
                return true;
            }
        };

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gameInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void addMenuActorListeners(final TextButton prevButton, final TextButton nextButton, final TextButton quitButton) {
        prevButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                model.prevStep();
            }
        });

        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                model.nextStep();
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setMenuScreen();
            }
        });
    }

    public void addInputProcessor(Stage stage) {
        this.inputMultiplexer.addProcessor(0, stage);
    }

    public TutorialModel getModel() {
        return model;
    }
}
