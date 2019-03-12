package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.rngg.models.GameModel;
import com.rngg.views.MenuView;

public class GameController extends Controller {

    public GameModel gameModel;

    public GameController(Game game, final GameModel gameModel) {
        super(game);
        this.gameModel = gameModel;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 unprojectedCoords = new Vector3(screenX, screenY, 0);
                camera.unproject(unprojectedCoords);

                gameModel.click(unprojectedCoords);
                return true;
            }
        });
    }

    @Override
    public void update(float delta) {
        Gdx.app.debug(this.getClass().getSimpleName(), "update");

        gameModel.playerScore++;

        if (Gdx.input.isKeyPressed(Input.Keys.B))
            game.setScreen(new MenuView(new MenuController(game)));
    }

    public int getPlayerScore() {
        return gameModel.playerScore;
    }

}
