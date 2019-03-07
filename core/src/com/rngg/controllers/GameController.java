package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.rngg.models.GameModel;

public class GameController extends Controller {
    GameModel gameModel;

    public GameController(Game game, GameModel gameModel) {
        super(game);
        this.gameModel = gameModel;
    }

    @Override
    public void update(float delta) {
        System.out.println("GameController update");
        gameModel.playerScore++;
    }

    public int getPlayerScore() {
        return gameModel.playerScore;
    }

}
