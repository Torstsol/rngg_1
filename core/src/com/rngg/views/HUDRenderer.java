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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rngg.configuration.GamePreferences;
import com.rngg.controllers.GameController;
import com.rngg.game.Rngg;
import com.rngg.models.GameModel;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;
import com.rngg.utils.RNG;
import com.rngg.utils.Utils;

import java.util.Arrays;

public class HUDRenderer {

    private OrthographicCamera HUDCamera;
    private SpriteBatch batch;
    private BitmapFont font;
    private GameModel gameModel;
    private Stage stage;
    private GamePreferences pref;
    private float startTime;
    private float elapsedTime;
    private String tempValues;
    private String[] attack, defend;
    private boolean attackStop = false;
    private boolean defendStop = false;
    private int attackIndex = 0;
    private int defendIndex = 0;
    private float totalRouletteTime;
    private int numDice;
    private RNG rng;
    private ShapeRenderer sr;
    private ShapeRenderer.ShapeType shapeType;

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
    private static final int youPosX = Rngg.WIDTH * 175 / 400;
    private static final int youPosY = Rngg.HEIGHT * 39 / 40;
    private static final int defPosX = Rngg.WIDTH * 295 / 400;
    private static final int defPosY = Rngg.HEIGHT * 39 / 40;

    private static final int attackPosX = Rngg.WIDTH * 205 / 400;
    private static final int attackPosY = Rngg.HEIGHT * 39 / 40;
    private static final int defendPosX = Rngg.WIDTH * 322 / 400;
    private static final int defendPosY = Rngg.HEIGHT * 39 / 40;


    public HUDRenderer(GameModel gameModel, BitmapFont font, GameController controller, ShapeRenderer sr) {
        HUDCamera = new OrthographicCamera();
        HUDCamera.setToOrtho(false, Rngg.WIDTH, Rngg.HEIGHT);
        batch = Utils.getSpriteBatch();
        this.font = font;
        this.gameModel = gameModel;
        pref = GamePreferences.getInstance();
        numDice = controller.gameModel.getNumDice();
        tempValues = "";
        totalRouletteTime = numDice * 0.2f;
        rng = RNG.getInstance();
        this.sr = sr;
        this.shapeType = ShapeRenderer.ShapeType.Filled;

        final TextButton defendAllButton = new TextButton("Defend All", GameAssetManager.getManager().get(Assets.SKIN));

        final TextButton defendCoreButton = new TextButton("Defend Core", GameAssetManager.getManager().get(Assets.SKIN));

        final TextButton defendFrontierButton = new TextButton("Defend Frontier", GameAssetManager.getManager().get(Assets.SKIN));

        final TextButton menuButton = new TextButton("In-game menu", GameAssetManager.getManager().get(Assets.SKIN));

        FitViewport fitViewport = new FitViewport(Rngg.WIDTH, Rngg.HEIGHT, HUDCamera);

        stage = new Stage(fitViewport, batch);

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
        group.padTop(Rngg.HEIGHT * 8 / 9); //Ghetto positioning
        controller.addInputProcessor(stage);
        controller.addActorListeners(defendAllButton, defendCoreButton, defendFrontierButton, menuButton);
    }

    public void draw() {
        HUDCamera.update();
        batch.setProjectionMatrix(HUDCamera.combined);

        String valueString = Arrays.toString(gameModel.getAttackValues());

        if (gameModel.isGameOver()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            sr.begin(shapeType);
            sr.setColor(0, 0, 0, 0.7f);
            sr.rect(0, 0, Rngg.WIDTH, Rngg.HEIGHT);
            sr.end();
        }

        if (tempValues.isEmpty() || !valueString.equals(tempValues)) {
            startTime = System.nanoTime();
            attack = gameModel.getAttackValues();
            defend = gameModel.getDefendValues();
            attackStop = false;
            defendStop = false;
        }
        elapsedTime = (System.nanoTime() - startTime) / 1000000000.0f;

        batch.begin();
        font.setColor(gameModel.currentPlayer().getColor());
        font.draw(batch, "Playing: " + gameModel.getPlayer(gameModel.getPlayerIndex()).getName(), 10, Rngg.HEIGHT * 39 / 40);
        font.setColor(Color.WHITE);
        if (gameModel.getAttackRoll() != 0 && gameModel.getDefendRoll() != 0) {
            font.draw(batch, "You: ", youPosX, youPosY);
            font.draw(batch, "Def: ", defPosX, defPosY);
            if (elapsedTime <= totalRouletteTime) {
                int i = 0;
                float interval = 0;
                while (interval < elapsedTime) {
                    interval += 0.2f;
                    if (elapsedTime > interval - 0.2f && elapsedTime <= interval) {
                        drawDice(i, attack, defend);
                    }
                    i++;
                }
            } else {
                drawDice(numDice, attack, defend);
            }

            tempValues = valueString;
        }

        if (gameModel.isGameOver()) {
            font.draw(batch, "GAME OVER! " + gameModel.getGameWinner().getName() + " won the game!", Rngg.WIDTH * 3 / 10, Rngg.HEIGHT * 11 / 20);
        }

        batch.end();


        stage.draw();

    }

    private void drawDice(int index, String[] att, String[] def) {
        if (att.length > index) {
            font.draw(batch, diceString(att, index, true), attackPosX, attackPosY);
        } else if (attackStop) {
            font.draw(batch, diceString(att, attackIndex, false), attackPosX, attackPosY);
        } else {
            font.draw(batch, diceString(att, index, false), attackPosX, attackPosY);
            attackIndex = index;
            attackStop = true;
        }
        if (def.length > index) {
            font.draw(batch, diceString(def, index, true), defendPosX, defendPosY);
        } else if (defendStop) {
            font.draw(batch, diceString(def, defendIndex, false), defendPosX, defendPosY);
        } else {
            font.draw(batch, diceString(def, index, false), defendPosX, defendPosY);
            defendIndex = index;
            defendStop = true;
        }
    }

    private String diceString(String[] values, int index, boolean random) {
        String str = "";
        for (int i = 0; i < index; i++) {
            str = str + values[i] + " ";
        }
        if (random) {
            for (int i = index; i < values.length; i++) {
                //int rand = rng.nextInt(pref.getDiceType().substring(1));
                int rand = RNG.nextInt(6);
                str = str + rand + " ";
            }
        }
        return str;
    }

}
