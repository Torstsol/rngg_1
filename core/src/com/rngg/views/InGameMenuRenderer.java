package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rngg.controllers.GameController;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;
import com.rngg.utils.Utils;

public class InGameMenuRenderer {
    private OrthographicCamera InGameMenuCamera;
    private SpriteBatch batch;
    private BitmapFont font;
    private GameModel gameModel;
    private Stage stage;
    private GameController controller;
    private boolean inGameMenuOpen;
    private ShapeRenderer sr;
    private ShapeRenderer.ShapeType shapeType;


    public InGameMenuRenderer(GameModel gameModel, BitmapFont font, GameController controller, ShapeRenderer sr) {
        InGameMenuCamera = new OrthographicCamera();
        InGameMenuCamera.setToOrtho(false, Rngg.WIDTH, Rngg.HEIGHT);
        batch = Utils.getSpriteBatch();
        this.font = font;
        this.gameModel = gameModel;
        this.inGameMenuOpen = false;
        this.sr = sr;
        this.shapeType = ShapeRenderer.ShapeType.Filled;

        final TextButton backButton = new TextButton("Main Menu", GameAssetManager.getManager().get(Assets.SKIN));

        final TextButton menuButton = new TextButton("Close", GameAssetManager.getManager().get(Assets.SKIN));

        FitViewport fitViewport = new FitViewport(Rngg.WIDTH, Rngg.HEIGHT, InGameMenuCamera);

        stage = new Stage(fitViewport, batch);

        Table table = new Table();
        table.setFillParent(true);

        VerticalGroup group = new VerticalGroup();
        group.grow();
        group.space(8);
        table.add(group);

        stage.addActor(table);
        group.addActor(backButton);
        group.addActor(menuButton);
        //group.padTop(Rngg.HEIGHT*8/9); //Ghetto positioning
        this.controller = controller;
        this.controller.addInGameMenuActorListeners(backButton, menuButton);
    }

    public void draw() {
        //TODO: This part can be changed by using a listener (Observer pattern) instead
        if (inGameMenuOpen != gameModel.isInGameMenuOpen()) {
            inGameMenuOpen = !inGameMenuOpen;
            if (inGameMenuOpen) {
                this.controller.addInputProcessor(stage);
            } else {
                this.controller.removeInputProcessor();
            }
        }
        if (inGameMenuOpen) {
            InGameMenuCamera.update();
            batch.setProjectionMatrix(InGameMenuCamera.combined);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            sr.begin(shapeType);
            sr.setColor(0, 0, 0, 0.7f);
            sr.rect(Rngg.WIDTH / 2 - Rngg.WIDTH * 1 / 10, Rngg.HEIGHT / 2 - Rngg.HEIGHT * 1 / 10, Rngg.WIDTH * 2 / 10, Rngg.HEIGHT * 2 / 10);
            sr.end();
            stage.draw();
        }
    }

}
