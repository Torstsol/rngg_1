package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
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

import java.util.ArrayList;
import java.util.Random;

public class HUDRenderer {

    private OrthographicCamera HUDCamera;
    private SpriteBatch HUDBatch;
    private BitmapFont font;
    private GameModel gameModel;
    private Stage stage;
    private GamePreferences pref;
    private float startTime;
    private float elapsedTime;
    private ArrayList<Float> tempValues;
    private static final Random generator = new Random();
    private boolean attackStop = false;
    private boolean defendStop = false;
    private int attackIndex = 0;
    private int defendIndex = 0;

    /* ON TOP OF EACH OTHER (MIGHT COLLIDE WITH MAP)
    private static final int youPosX = Rngg.WIDTH*27/40;
    private static final int youPosY = Rngg.HEIGHT*39/40;
    private static final int defPosX = Rngg.WIDTH*27/40;
    private static final int defPosY = Rngg.HEIGHT*35/40;

    private static final int attackPosX = Rngg.WIDTH*30/40;
    private static final int attackPosY = Rngg.HEIGHT*39/40;
    private static final int defendPosX = Rngg.WIDTH*327/400;
    private static final int defendPosY = Rngg.HEIGHT*35/40;
    */

    // SIDE BY SIDE (MIGHT COLLIDE WITH USERNAMES)
    private static final int youPosX = Rngg.WIDTH*15/40;
    private static final int youPosY = Rngg.HEIGHT*39/40;
    private static final int defPosX = Rngg.WIDTH*27/40;
    private static final int defPosY = Rngg.HEIGHT*39/40;

    private static final int attackPosX = Rngg.WIDTH*18/40;
    private static final int attackPosY = Rngg.HEIGHT*39/40;
    private static final int defendPosX = Rngg.WIDTH*327/400;
    private static final int defendPosY = Rngg.HEIGHT*39/40;


    public HUDRenderer(GameModel gameModel, BitmapFont font, GameAssetManager assetManager, GameController controller) {
        HUDCamera = new OrthographicCamera();
        HUDCamera.setToOrtho(false, Rngg.WIDTH, Rngg.HEIGHT);
        HUDBatch = new SpriteBatch();
        this.font = font;
        this.gameModel = gameModel;
        pref = GamePreferences.getInstance();
        tempValues = new ArrayList<Float>();

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

        if(tempValues.isEmpty() || !gameModel.getAttackValues().equals(tempValues)){
            startTime = System.nanoTime();
            attackStop = false;
            defendStop = false;
        }
        elapsedTime = (System.nanoTime() - startTime)/1000000000.0f;

        HUDBatch.begin();
        font.setColor(gameModel.currentPlayer().getColor());
        font.draw(HUDBatch,"Player " + gameModel.getPlayerIndex(),10,Rngg.HEIGHT*39/40);
        font.setColor(Color.WHITE);
        font.draw(HUDBatch,"is playing",150,Rngg.HEIGHT*39/40);
        if(gameModel.getAttackRoll() != 0 && gameModel.getDefendRoll() != 0){
            font.draw(HUDBatch,"You: ", youPosX,youPosY);
            font.draw(HUDBatch,"Defender: ", defPosX,defPosY);
            if(elapsedTime<=0.2f){
                drawDice(0);
            }
            if(elapsedTime>0.2f && elapsedTime<=0.4f){
                drawDice(1);
            }
            if(elapsedTime>0.4f && elapsedTime<=0.6f){
                drawDice(2);
            }
            if(elapsedTime>0.6f && elapsedTime<=0.8f){
                drawDice(3);
            }
            if(elapsedTime>0.8f && elapsedTime<=1f){
                drawDice(4);
            }
            if(elapsedTime>1f && elapsedTime<=1.2f){
                drawDice(5);
            }
            if(elapsedTime>1.2f && elapsedTime<=1.4f){
                drawDice(6);
            }
            if(elapsedTime>1.4f && elapsedTime<=1.6f){
                drawDice(7);
            }
            if(elapsedTime>1.6f){
                drawDice(8);
            }

            tempValues = gameModel.getAttackValues();
        }

        HUDBatch.end();
        stage.draw();

    }

    private void drawDice(int index) {
        if(gameModel.getAttackValues().size() > index){
            font.draw(HUDBatch, diceString(gameModel.getAttackValues(), index, true), attackPosX, attackPosY);
        } else if(attackStop){
            font.draw(HUDBatch, diceString(gameModel.getAttackValues(), attackIndex, false), attackPosX, attackPosY);
        } else {
            font.draw(HUDBatch, diceString(gameModel.getAttackValues(), index, false), attackPosX, attackPosY);
            attackIndex = index;
            attackStop = true;
        }
        if(gameModel.getDefendValues().size() > index){
            font.draw(HUDBatch, diceString(gameModel.getDefendValues(), index, true), defendPosX, defendPosY);
        } else if(defendStop){
            font.draw(HUDBatch, diceString(gameModel.getDefendValues(), defendIndex, false), defendPosX, defendPosY);
        } else {
            font.draw(HUDBatch, diceString(gameModel.getDefendValues(), index, false), defendPosX, defendPosY);
            defendIndex = index;
            defendStop = true;
        }
    }

    private String diceString(ArrayList<Float> values, int index, boolean random) {
        String str = "";
        for(int i = 0; i < index; i++){
            str = str + values.get(i).shortValue() + " ";
        }
        if(random){
            for(int i = index; i < values.size(); i++){
                int rand = generator.nextInt(8);
                str = str + rand + " ";
            }
        }
        return str;
    }

}
