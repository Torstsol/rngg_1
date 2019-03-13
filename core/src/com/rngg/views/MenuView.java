package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.controllers.MenuController;
import com.rngg.utils.GameAssetManager;

public class MenuView extends View {

    MenuController controller;

    private SpriteBatch batch;

    public MenuView(GameAssetManager assetManager, MenuController controller) {
        super(assetManager);

        this.controller = controller;

        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        controller.update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, ">Menu View<", 50, 250);
        font.draw(batch, "Press 'l' to go to lobby view", 50, 150);
        font.draw(batch, "Press 's' to go to settings view", 50, 50);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
