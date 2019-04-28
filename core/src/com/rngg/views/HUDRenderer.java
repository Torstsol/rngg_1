package com.rngg.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rngg.configuration.GamePreferences;
import com.rngg.controllers.GameController;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;

public class HUDRenderer {

    private OrthographicCamera HUDCamera;
    private SpriteBatch HUDBatch;
    private BitmapFont font;
    private GameModel gameModel;
    private Stage stage;
    private GamePreferences pref;


    public HUDRenderer(GameModel gameModel, BitmapFont font, GameAssetManager assetManager, GameController controller) {
        HUDCamera = new OrthographicCamera();
        HUDCamera.setToOrtho(false, Rngg.WIDTH, Rngg.HEIGHT);
        HUDBatch = new SpriteBatch();
        this.font = font;
        this.gameModel = gameModel;
        pref = GamePreferences.getInstance();

        final TextButton defendAllButton = new TextButton("Defend All", assetManager.manager.get(Assets.SKIN));

        final TextButton defendCoreButton = new TextButton("Defend Core", assetManager.manager.get(Assets.SKIN));

        final TextButton defendFrontierButton = new TextButton("Defend Frontier", assetManager.manager.get(Assets.SKIN));

        final TextButton menuButton = new TextButton("In-game menu", assetManager.manager.get(Assets.SKIN));

        FitViewport fitViewport = new FitViewport(Rngg.WIDTH, Rngg.HEIGHT, HUDCamera);

        stage = new Stage(fitViewport, HUDBatch);

        Table table = new Table();
        table.setFillParent(true);

        HorizontalGroup group = new HorizontalGroup();
        group.grow();
        group.space(8);
        table.add(group);

        stage.addActor(table);
        group.addActor(defendAllButton);
        group.addActor(defendCoreButton);
        group.addActor(defendFrontierButton);
        group.addActor(menuButton);
        group.padTop(Rngg.HEIGHT*8/9); //Ghetto positioning
        controller.addInputProcessor(stage);
        controller.addActorListeners(defendAllButton, defendCoreButton, defendFrontierButton, menuButton);
    }

    public void draw() {
        HUDCamera.update();
        HUDBatch.setProjectionMatrix(HUDCamera.combined);

        HUDBatch.begin();
        font.setColor(gameModel.currentPlayer().getColor());
        font.draw(HUDBatch,"Player " + gameModel.getPlayer(gameModel.getPlayerIndex()).getName() + " Host: " + gameModel.host.getName(),10,Rngg.HEIGHT*39/40);
        font.setColor(Color.WHITE);
        if(gameModel.getAttackRoll() != 0 && gameModel.getDefendRoll() != 0){
            font.draw(HUDBatch,"You rolled: " + gameModel.getAttackRoll() + " |  Defender rolled: " + gameModel.getDefendRoll(), Rngg.WIDTH*21/40,Rngg.HEIGHT*39/40);
        }

        HUDBatch.end();
        stage.draw();
    }

}
