package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rngg.controllers.GameController;
import com.rngg.models.SquareMap;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;

public class GameView extends View {

    private GameController controller;

    private SpriteBatch batch;
    private BitmapFont font;
    private MapRenderer mapRenderer;
    private ShapeRenderer sr;

    public GameView(GameAssetManager assetManager, GameController controller) {
        super(assetManager);

        this.controller = controller;

        batch = new SpriteBatch();
        font = assetManager.manager.get(Assets.MINECRAFTIA);
        font.setColor(Color.WHITE);
        mapRenderer = new SquareMapRenderer((SquareMap) controller.gameModel.getMap(), font);

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

        mapRenderer.draw();
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
