package com.rngg.views;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.controllers.SettingsController;

public class SettingsView extends View {
    SettingsController controller;

    SpriteBatch batch;

    public SettingsView(SettingsController controller) {
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