package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rngg.controllers.GameController;
import com.rngg.game.Rngg;

public class GameView extends View {

    private GameController controller;

    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer sr;

    public GameView(GameController controller) {
        this.controller = controller;

        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("minecraftia.fnt"),
                Gdx.files.internal("minecraftia.png"), false);
        font.setColor(Color.WHITE);

        this.sr = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);
        controller.update(delta);
        controller.setCamera(camera);

        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.gameModel.draw(this);
    }

    public ShapeRenderer getSR() {
        return this.sr;
    }

    public BitmapFont getFont() {
        return font;
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }
}
