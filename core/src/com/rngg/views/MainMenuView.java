package com.rngg.views;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.controllers.MainMenuController;

public class MainMenuView extends View {
    MainMenuController controller;

    SpriteBatch batch;

    public MainMenuView(MainMenuController controller) {
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