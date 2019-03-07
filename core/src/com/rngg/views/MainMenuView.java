package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.controllers.Controller;

public class MainMenuView extends View {
    SpriteBatch batch;
    Controller controller;

    public MainMenuView(Controller controller) {
        this.controller = controller;

        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        controller.update(Gdx.graphics.getDeltaTime());

        batch.begin();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}