/*
 * Super class for views which implements all methods from the screen interface.
 * It also create common resources
 */

package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.game.Rngg;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;
import com.rngg.utils.Utils;

abstract public class View implements Screen {

    protected OrthographicCamera camera;
    protected BitmapFont font; // default font
    protected AssetManager assetManager;
    protected SpriteBatch batch = Utils.getSpriteBatch();

    public View() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Rngg.WIDTH, Rngg.HEIGHT);

        font = GameAssetManager.getManager().get(Assets.FONT);
        assetManager = GameAssetManager.getManager();
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
