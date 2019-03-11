package com.rngg.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;
import com.rngg.models.SquareZone;
import com.rngg.views.MenuView;

public class GameController extends Controller {

    public GameModel gameModel;
    private SquareZone clickedZone = null;

    public GameController(Game game, final GameModel gameModel) {
        super(game);
        this.gameModel = gameModel;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (clickedZone != null) {
                    clickedZone.unClick();
                }
                clickedZone = gameModel.getMap().screenCoordToZone(new Vector2(screenX, Rngg.HEIGHT - screenY));
                clickedZone.click();
                Gdx.app.log(this.getClass().getSimpleName(), "Clicked " + clickedZone.toString());
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
