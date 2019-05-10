package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;
import com.rngg.services.IPlayServices;

public class GameController extends Controller {

    public final GameModel gameModel;
    InputMultiplexer inputMultiplexer;

    public GameController(final Rngg game, final GameModel gameModel) {
        super(game);
        this.gameModel = gameModel;
        InputProcessor gameInputProcessor = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 unprojectedCoords = new Vector3(screenX, screenY, 0);
                camera.unproject(unprojectedCoords);

                GameController.this.gameModel.click(unprojectedCoords);
                return true;
            }

            // Switch current player, for debugging purposes
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.NUM_1:
                        gameModel.setPlayer(0);
                        break;
                    case Input.Keys.NUM_2:
                        gameModel.setPlayer(1);
                        break;
                    case Input.Keys.NUM_3:
                        gameModel.setPlayer(2);
                        break;
                    case Input.Keys.NUM_4:
                        gameModel.setPlayer(3);
                        break;
                    case Input.Keys.A:
                        gameModel.defend(gameModel.getPlayerIndex(), GameModel.DEFEND_ALL, true);
                        break;
                    case Input.Keys.F:
                        gameModel.defend(gameModel.getPlayerIndex(), GameModel.DEFEND_FRONTIER, true);
                        break;
                    case Input.Keys.C:
                        gameModel.defend(gameModel.getPlayerIndex(), GameModel.DEFEND_CORE, true);
                        break;
                    case Input.Keys.I:
                        gameModel.updateInGameMenu();
                        break;
                    default:
                        break;
                }
                return true;
            }
        };

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gameInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        IPlayServices playServices = game.getAPI();
        if (!Rngg.RUN_DESKTOP) {
            playServices.setRealTimeListener(gameModel);
        }
    }

    public void addInputProcessor(Stage stage) {
        inputMultiplexer.addProcessor(0, stage);
    }

    public void removeInputProcessor() {
        inputMultiplexer.removeProcessor(0);
    }

    public void addActorListeners(final TextButton defendAllButton, final TextButton defendCoreButton, final TextButton defendFrontierButton, final TextButton menuButton) {
        defendAllButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameModel.defend(gameModel.getPlayerIndex(), GameModel.DEFEND_ALL, true);
            }
        });

        defendCoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameModel.defend(gameModel.getPlayerIndex(), GameModel.DEFEND_CORE, true);
            }
        });

        defendFrontierButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameModel.defend(gameModel.getPlayerIndex(), GameModel.DEFEND_FRONTIER, true);
            }
        });

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameModel.updateInGameMenu();
            }
        });
    }

    public void addInGameMenuActorListeners(final TextButton backButton, final TextButton menuButton) {
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!Rngg.RUN_DESKTOP) game.getAPI().leaveGame();
                game.screenManager.setMenuScreen();
            }
        });

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameModel.updateInGameMenu();
            }
        });
    }

}