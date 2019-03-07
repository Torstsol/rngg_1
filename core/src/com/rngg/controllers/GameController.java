package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.rngg.models.GameModel;

public class GameController extends Controller {
    GameModel model;

    public GameController(Game game, GameModel model) {
        super(game);
        this.model = model;
    }

    @Override
    public void update(float delta) {
        System.out.println("GameController update");
        model.setPlayerScore(model.getPlayerScore() + 1);
        System.out.println(model.getPlayerScore());
    }

    @Override
    public void handleInput(Input.Keys key) {

    }
}
