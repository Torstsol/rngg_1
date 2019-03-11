package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.models.GameModel;
import com.rngg.views.GameView;
import com.rngg.views.MenuView;

public class GameController extends Controller {

    GameModel gameModel;

    public GameController(Game game, GameModel gameModel) {
        super(game);
        this.gameModel = gameModel;
    }

    @Override
    public void update(float delta) {
        Gdx.app.log(this.getClass().getSimpleName(), "update");

        gameModel.playerScore++;

        if(Gdx.input.isKeyPressed(Input.Keys.B))
            game.setScreen(new MenuView(new MenuController(game), game));
    }

    public int getPlayerScore() {
        return gameModel.playerScore;
    }

}
