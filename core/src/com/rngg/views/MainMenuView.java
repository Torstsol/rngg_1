package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.controllers.Controller;

public class MainMenuView extends View {
    SpriteBatch batch;
    Texture img;
    Controller controller;

    public MainMenuView(Controller controller) {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        this.controller = controller;
    }

    @Override
    public void update(float delta) {
        System.out.println("Updating MainMenuView");
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            controller.setView();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}