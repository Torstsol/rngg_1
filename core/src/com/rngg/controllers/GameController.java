package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;

public class GameController extends Controller {

    public final GameModel gameModel;

    public GameController(final Rngg game, final GameModel gameModel) {
        super(game);
        this.gameModel = gameModel;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 unprojectedCoords = new Vector3(screenX, screenY, 0);
                camera.unproject(unprojectedCoords);

                GameController.this.gameModel.click(unprojectedCoords);
                return true;
            }

            // Switch current player, for debugging purposes
            @Override public boolean keyDown(int keycode) {
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
                        gameModel.defend(gameModel.getPlayerIndex(), gameModel.DEFEND_ALL);
                    case Input.Keys.F:
                        gameModel.defend(gameModel.getPlayerIndex(), gameModel.DEFEND_FRONTIER);
                    case Input.Keys.C:
                        gameModel.defend(gameModel.getPlayerIndex(), gameModel.DEFEND_CORE);
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void update(float delta) {
        gameModel.playerScore++;

        if(Gdx.input.isKeyPressed(Input.Keys.B))
            game.screenManager.setMenuScreen();
    }

    public int getPlayerScore() {
        return gameModel.playerScore;
    }

}
