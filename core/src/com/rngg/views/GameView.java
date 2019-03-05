package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameView extends View {
    public GameView() {

    }

    @Override
    public void update(float delta) {
        System.out.println("Updating GameView");
    }

    @Override public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
