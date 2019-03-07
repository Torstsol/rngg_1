package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.rngg.controllers.Controller;

public class GameView extends View {
    private Controller controller;

    public GameView(Controller controller) {
        this.controller = controller;
    }

    @Override public void render(float delta) {
        controller.update(delta);

        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
