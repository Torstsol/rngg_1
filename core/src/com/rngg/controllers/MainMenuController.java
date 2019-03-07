package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.models.GameModel;
import com.rngg.views.GameView;

public class MainMenuController extends Controller {
    public MainMenuController(Game game) {
        super(game);
    }

    @Override
    public void update(float delta) {
        System.out.println("MainMenuController update");

        if(Gdx.input.isKeyPressed(Input.Keys.A))
            game.setScreen(new GameView(new GameController(game, new GameModel())));
    }

}
