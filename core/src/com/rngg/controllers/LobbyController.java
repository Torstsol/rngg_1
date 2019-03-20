package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;
import com.rngg.models.WaitingRoomModel;

import javax.xml.soap.Text;

public class LobbyController extends Controller {

    public LobbyController(Rngg game) {
        super(game);
    }

    @Override
    public void update(float delta) {}

    public void setInputProcessor(Stage stage) {
        Gdx.input.setInputProcessor(stage);
    }

    public void addActorListeners(final TextButton quickGameButton, final TextButton invitePlayersButton, final TextButton localGameButton, final TextButton menuButton) {
        localGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setGameScreen(new GameModel(4), null);
            }
        });

        quickGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setWaitingRoomScreen(new WaitingRoomModel(), true);
            }
        });

        invitePlayersButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setWaitingRoomScreen(new WaitingRoomModel(), false);
            }
        });

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setMenuScreen();
            }
        });
    }


}
