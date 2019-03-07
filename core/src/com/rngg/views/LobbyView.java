package com.rngg.views;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.controllers.LobbyController;

public class LobbyView extends View {
    LobbyController controller;

    SpriteBatch batch;

    public LobbyView(LobbyController controller) {
        this.controller = controller;

        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        controller.update(delta);

        batch.begin();
        // TODO: Render
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}