package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;
import com.rngg.views.GameView;
import com.rngg.views.MenuView;

public class LobbyController extends Controller {

    public LobbyController(Rngg game) {
        super(game);
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.G))
            game.setScreen(new GameView(game.assetManager, new GameController(game, new GameModel())));
        else if(Gdx.input.isKeyPressed(Input.Keys.B))
            game.setScreen(new MenuView(game.assetManager, new MenuController(game)));
        else if(Gdx.input.isKeyPressed(Input.Keys.L)){
            //((Rngg) game).IPlayServices.startSignInIntent();
        }
    }

}
