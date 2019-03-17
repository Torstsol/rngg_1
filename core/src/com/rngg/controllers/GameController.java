package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;

public class GameController extends Controller {

    public final GameModel gameModel;

    public GameController(Rngg game, GameModel gameModel) {
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
