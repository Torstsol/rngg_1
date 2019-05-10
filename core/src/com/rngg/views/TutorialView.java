/*
 * View for the tutorial screen.
 */

package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rngg.controllers.TutorialController;
import com.rngg.game.Rngg;
import com.rngg.models.GameMap;
import com.rngg.models.HexMap;
import com.rngg.models.HexMeshMap;
import com.rngg.models.SquareMap;
import com.rngg.utils.Utils;

public class TutorialView extends View {

    private TutorialController controller;

    private SpriteBatch batch;
    private MapRenderer mapRenderer;
    private ShapeRenderer shapeRenderer;
    private TutorialMenuRenderer tutorialMenuRenderer;

    public TutorialView(TutorialController controller) {
        this.controller = controller;
        this.shapeRenderer = new ShapeRenderer();

        camera.viewportHeight = (float) (Rngg.HEIGHT * 10 / 8);
        batch = Utils.getSpriteBatch();
        font.setColor(Color.WHITE);

        mapRenderer = getMapRenderer();
        tutorialMenuRenderer = new TutorialMenuRenderer(controller.getModel(), font, controller, shapeRenderer);
    }

    @Override
    public void render(float delta) {
        this.mapRenderer = this.getMapRenderer();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        controller.setCamera(camera);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.draw();
        tutorialMenuRenderer.draw();
    }

    private MapRenderer getMapRenderer() {
        GameMap map = controller.getModel().getMap();

        if (map instanceof SquareMap) {
            return new SquareMapRenderer((SquareMap) map, shapeRenderer, batch, font);
        } else if (map instanceof HexMap) {
            return new HexMapRenderer((HexMap) map, shapeRenderer, batch, font);
        } else if (map instanceof HexMeshMap) {
            return new HexMeshMapRenderer((HexMeshMap) map, shapeRenderer, batch, font);
        }

        return null;
    }

    public BitmapFont getFont() {
        return font;
    }

}
