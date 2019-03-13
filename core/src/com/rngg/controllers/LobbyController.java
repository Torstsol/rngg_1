package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;

public class LobbyController extends Controller {

    public LobbyController(Rngg game) {
        super(game);
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.G))
            game.screenManager.setGameScreen(new GameModel());
        else if(Gdx.input.isKeyPressed(Input.Keys.B))
            game.screenManager.setMenuScreen();
        else if(Gdx.input.isKeyPressed(Input.Keys.L)){
            //((Rngg) game).IPlayServices.startSignInIntent();
        }
    }

}
