package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.controllers.LobbyController;
import com.rngg.utils.GameAssetManager;

public class LobbyView extends View {

    LobbyController controller;

    private SpriteBatch batch;

    public LobbyView(GameAssetManager assetManager, LobbyController controller) {
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
        font.draw(batch, ">Lobby View<", 50, 250);
        font.draw(batch, "Press 'g' to go to game view", 50, 150);
        font.draw(batch, "Press 'b' to go back", 50, 50);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
