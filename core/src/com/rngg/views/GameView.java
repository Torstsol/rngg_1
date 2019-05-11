/*
 * View for the actual game.
 */

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
import com.rngg.models.HexMeshMap;
import com.rngg.models.SquareMap;
import com.rngg.utils.Utils;

public class GameView extends View {

    private GameController controller;

    private SpriteBatch batch;
    private MapRenderer mapRenderer;
    private ShapeRenderer shapeRenderer;
    private HUDRenderer hudRenderer;
    private InGameMenuRenderer inGameMenuRenderer;

    public GameView(GameController controller) {
        this.controller = controller;

        camera.viewportHeight = (float) (Rngg.HEIGHT * 10 / 8);
        batch = Utils.getSpriteBatch();
        font.setColor(Color.WHITE);
        shapeRenderer = new ShapeRenderer();
        mapRenderer = getMapRenderer();
        hudRenderer = new HUDRenderer(controller.gameModel, font, controller, shapeRenderer);
        inGameMenuRenderer = new InGameMenuRenderer(controller.gameModel, font, controller, shapeRenderer);
    }

    @Override
    public void render(float delta) {
        mapRenderer = this.getMapRenderer();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

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
        } else if (map instanceof HexMeshMap) {
            return new HexMeshMapRenderer((HexMeshMap) map, shapeRenderer, batch, font);
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
