package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;

public class GameController extends Controller {

    GameModel gameModel;

    public GameController(Rngg game, GameModel gameModel) {
        super(game);
        this.gameModel = gameModel;
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
