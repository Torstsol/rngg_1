package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.controllers.GameController;

public class GameView extends View {

    private GameController controller;

    private SpriteBatch batch;
    private BitmapFont font;

    public GameView(GameController controller) {
        this.controller = controller;

        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("minecraftia.fnt"),
                Gdx.files.internal("minecraftia.png"), false);
    }

    @Override public void render(float delta) {
        controller.update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, Integer.toString(controller.getPlayerScore()), 50, 350);
        font.draw(batch, ">Game View<", 50, 250);
        font.draw(batch, "Press 'b' to go back", 50, 150);
        batch.end();
    }

}
