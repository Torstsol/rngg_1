package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rngg.controllers.GameController;
import com.rngg.models.GameMap;
import com.rngg.models.SquareMap;

public class GameView extends View {

    private GameController controller;

    private SpriteBatch batch;
    private BitmapFont font;
    private GameMap map;
    private ShapeRenderer sr;

    public GameView(GameController controller) {
        this.controller = controller;

        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("minecraftia.fnt"),
                Gdx.files.internal("minecraftia.png"), false);

        this.sr = new ShapeRenderer();

        //todo: Remove/replace hardcoded map
        this.map = new SquareMap(9,16);
    }

    @Override public void render(float delta) {
        controller.update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        map.draw(sr);
        batch.begin();

        batch.end();
    }

}
