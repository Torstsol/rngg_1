package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.rngg.controllers.GameController;
import com.rngg.game.Rngg;
import com.rngg.models.Player;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;

import java.util.List;

public class GameView extends View {

    private GameController controller;

    private SpriteBatch batch;
    private BitmapFont font;
    private MapRenderer mapRenderer;
    private ShapeRenderer sr;
    private List<Player> players;
    private HUDRenderer hudRenderer;


    public GameView(GameAssetManager assetManager, GameController controller, List<Player> players) {
        super(assetManager);
        camera.viewportHeight = Rngg.HEIGHT*10/8;

        this.controller = controller;
        this.players = players;

        batch = new SpriteBatch();
        font = assetManager.manager.get(Assets.MINECRAFTIA);
        font.setColor(Color.WHITE);
        this.sr = new ShapeRenderer();
        mapRenderer = new SquareMapRenderer(controller.gameModel.getMap(), sr, batch, font);
        hudRenderer = new HUDRenderer(controller.gameModel, font, assetManager, controller);
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        controller.update(delta);
        controller.setCamera(camera);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.draw();
        hudRenderer.draw();
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
