package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rngg.configuration.GamePreferences;
import com.rngg.controllers.Controller;
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
    private ShapeRenderer sr;
    private ShapeRenderer.ShapeType shapeType;


    public HUDRenderer(GameModel gameModel, BitmapFont font, GameAssetManager assetManager, GameController controller, ShapeRenderer sr) {
        HUDCamera = new OrthographicCamera();
        HUDCamera.setToOrtho(false, Rngg.WIDTH, Rngg.HEIGHT);
        HUDBatch = new SpriteBatch();
        this.font = font;
        this.gameModel = gameModel;
        this.sr = sr;
        this.shapeType = ShapeRenderer.ShapeType.Filled;

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
        group.padTop(Rngg.HEIGHT*145/160); //Ghetto positioning
        controller.addInputProcessor(stage);
        controller.addActorListeners(defendAllButton, defendCoreButton, defendFrontierButton, menuButton);
    }

    public void draw() {
        HUDCamera.update();
        HUDBatch.setProjectionMatrix(HUDCamera.combined);
        sr.setProjectionMatrix(HUDCamera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(shapeType);
        sr.setColor((float)86/255,(float)204/255,(float)114/255,0.5f);
        sr.rect(0,0, Rngg.WIDTH, Rngg.HEIGHT*29/288);
        sr.rect(0,Rngg.HEIGHT, Rngg.WIDTH, -Rngg.HEIGHT*29/288);
        sr.setColor((float)86/255,(float)204/255,(float)114/255,0.9f);
        sr.rect(0,Rngg.HEIGHT*15/160, Rngg.WIDTH, Rngg.HEIGHT*1/72);
        sr.rect(0,Rngg.HEIGHT-Rngg.HEIGHT*15/160, Rngg.WIDTH, -Rngg.HEIGHT*1/72);
        sr.end();

        HUDBatch.begin();
        font.setColor(gameModel.currentPlayer().getColor());
        font.draw(HUDBatch,"Player " + gameModel.getPlayerIndex(),10,Rngg.HEIGHT*39/40);
        font.setColor(Color.WHITE);
        font.draw(HUDBatch,"is playing",150,Rngg.HEIGHT*39/40);
        if(gameModel.getAttackRoll() != 0 && gameModel.getDefendRoll() != 0){
            font.draw(HUDBatch,"You rolled: " + gameModel.getAttackRoll() + " |  Defender rolled: " + gameModel.getDefendRoll(), Rngg.WIDTH*21/40,Rngg.HEIGHT*39/40);
        }
        HUDBatch.end();
        stage.draw();
    }

}
