package com.rngg.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenuView extends AbstractView {
    SpriteBatch batch;
    Texture img;

    public MainMenuView(Game game) {
        super(game);

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void update(float delta) {
        System.out.println("Updating MainMenuView");
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            game.setScreen(new GameView(game));
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