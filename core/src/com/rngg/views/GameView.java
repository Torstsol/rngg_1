package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.rngg.controllers.GameController;

public class GameView extends View {
    private GameController controller;

    public GameView(GameController controller) {
        this.controller = controller;
    }

    @Override public void render(float delta) {
        controller.update(delta);

        Gdx.gl.glClearColor(1, 1, 0, 1);
        System.out.println("From GameView: " + controller.getPlayerScore());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
