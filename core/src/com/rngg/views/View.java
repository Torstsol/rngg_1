package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.rngg.game.Rngg;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;

abstract public class View implements Screen {

    protected OrthographicCamera camera;
    protected GameAssetManager assetManager;
    protected BitmapFont font; // default font

    public View(GameAssetManager assetManager) {
        this.assetManager = assetManager;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Rngg.WIDTH, Rngg.HEIGHT);

        font = assetManager.manager.get(Assets.MINECRAFTIA);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
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

    }

}
