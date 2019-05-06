package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rngg.controllers.GameController;
import com.rngg.game.Rngg;
import com.rngg.models.GameMap;
import com.rngg.models.HexMap;
import com.rngg.models.Player;
import com.rngg.models.SquareMap;
import com.rngg.utils.GameAssetManager;

import java.util.List;

public class GameView extends View {

    private GameController controller;

    private SpriteBatch batch;
    private MapRenderer mapRenderer;
    private ShapeRenderer shapeRenderer;
    private List<Player> players;
    private HUDRenderer hudRenderer;
    private InGameMenuRenderer inGameMenuRenderer;

    public GameView(GameController controller) {
        camera.viewportHeight = (float) (Rngg.HEIGHT * 10 / 8);

        this.controller = controller;

        batch = new SpriteBatch();
        font.setColor(Color.WHITE);
        this.shapeRenderer = new ShapeRenderer();
        mapRenderer = getMapRenderer();
        hudRenderer = new HUDRenderer(controller.gameModel, font, controller, shapeRenderer);
        inGameMenuRenderer = new InGameMenuRenderer(controller.gameModel, font, controller, shapeRenderer);
    }

    @Override
    public void render(float delta) {
        this.mapRenderer = this.getMapRenderer();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        controller.update(delta);
        controller.setCamera(camera);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.draw();
        hudRenderer.draw();
        inGameMenuRenderer.draw();
    }

    private MapRenderer getMapRenderer() {
        GameMap map = controller.gameModel.getMap();

        if (map instanceof SquareMap) {
            return new SquareMapRenderer((SquareMap) map, shapeRenderer, batch, font);
        } else if (map instanceof HexMap) {
            return new HexMapRenderer((HexMap) map, shapeRenderer, batch, font);
        }

        return null;
    }

    public ShapeRenderer getSR() {
        return this.shapeRenderer;
    }

    public BitmapFont getFont() {
        return font;
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }
}
