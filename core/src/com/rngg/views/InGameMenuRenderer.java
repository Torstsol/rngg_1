package com.rngg.views;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rngg.controllers.GameController;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;

public class InGameMenuRenderer {
    private OrthographicCamera InGameMenuCamera;
    private SpriteBatch InGameMenuBatch;
    private BitmapFont font;
    private GameModel gameModel;
    private Stage stage;
    private GameController controller;
    private boolean inGameMenuOpen;

    public InGameMenuRenderer(GameModel gameModel, BitmapFont font, GameAssetManager assetManager, GameController controller) {
        InGameMenuCamera = new OrthographicCamera();
        InGameMenuCamera.setToOrtho(false, Rngg.WIDTH, Rngg.HEIGHT);
        InGameMenuBatch = new SpriteBatch();
        this.font = font;
        this.gameModel = gameModel;
        this.inGameMenuOpen = false;

        final TextButton backButton = new TextButton("Main Menu", assetManager.manager.get(Assets.SKIN));

        final TextButton menuButton = new TextButton("Close", assetManager.manager.get(Assets.SKIN));

        FitViewport fitViewport = new FitViewport(Rngg.WIDTH, Rngg.HEIGHT, InGameMenuCamera);

        stage = new Stage(fitViewport, InGameMenuBatch);

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
        if(inGameMenuOpen != gameModel.isInGameMenuOpen()){
            inGameMenuOpen = !inGameMenuOpen;
            if(inGameMenuOpen){
                this.controller.addInputProcessor(stage);
            }
            else{
                this.controller.removeInputProcessor();
            }
        }
        if(inGameMenuOpen){
            InGameMenuCamera.update();
            InGameMenuBatch.setProjectionMatrix(InGameMenuCamera.combined);
            stage.draw();
        }
    }

}
