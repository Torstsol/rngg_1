package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rngg.controllers.TutorialController;
import com.rngg.game.Rngg;
import com.rngg.models.TutorialModel;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;
import com.rngg.utils.Utils;

public class TutorialMenuRenderer {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private TutorialModel model;
    private Stage stage;
    private TutorialController controller;
    private final TextButton prevButton;
    private final TextButton nextButton;
    private final TextButton quitButton;
    private float timeCounter;
    private float textBeginX;
    private float textBeginY;

    public TutorialMenuRenderer(TutorialModel model, BitmapFont font, TutorialController controller, ShapeRenderer shapeRenderer) {
        this.font = font;
        this.model = model;
        this.prevButton = new TextButton("Prev step", GameAssetManager.getManager().get(Assets.SKIN));
        this.nextButton = new TextButton("Next step", GameAssetManager.getManager().get(Assets.SKIN));
        this.quitButton = new TextButton("Quit tutorial", GameAssetManager.getManager().get(Assets.SKIN));
        this.timeCounter = Gdx.graphics.getDeltaTime();
        this.textBeginX = Rngg.WIDTH - Rngg.WIDTH / 2.5f;
        this.textBeginY = (float) (Rngg.HEIGHT - Rngg.HEIGHT / 16);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Rngg.WIDTH, Rngg.HEIGHT);
        batch = Utils.getSpriteBatch();

        FitViewport fitViewport = new FitViewport(Rngg.WIDTH, Rngg.HEIGHT, camera);
        stage = new Stage(fitViewport, batch);

        Table table = new Table();
        table.setFillParent(true);

        HorizontalGroup group = new HorizontalGroup();
        group.grow();
        group.space(8);
        table.add(group);

        stage.addActor(table);
        group.addActor(prevButton);
        group.addActor(nextButton);
        group.addActor(quitButton);
        group.padTop((float) Rngg.HEIGHT * 8 / 9);

        this.controller = controller;
        this.controller.addInputProcessor(stage);
        this.controller.addMenuActorListeners(prevButton, nextButton, quitButton);
    }

    public void draw() {
        prevButton.setTouchable(model.hasPrev() ? Touchable.enabled : Touchable.disabled);
        prevButton.setColor(model.hasPrev() ? Color.WHITE : Color.GRAY);
        nextButton.setTouchable(model.hasNext() ? Touchable.enabled : Touchable.disabled);
        nextButton.setColor(model.hasNext() ? Color.WHITE : Color.GRAY);
        quitButton.setColor(Color.WHITE);

        int step = model.getStep();
        if (step > 8) {
            float dt = Gdx.graphics.getDeltaTime();
            timeCounter += dt;

            if (timeCounter > 50 * dt) {
                ((step == 9 || step == 10) ? nextButton : quitButton).setColor(Color.RED);
            }
            if (timeCounter > 100 * dt) {
                ((step == 9 || step == 10) ? nextButton : quitButton).setColor(Color.WHITE);
                timeCounter = 0f;
            }
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawTutorialText(batch, model.getStep());
        batch.end();
        stage.draw();
    }

    // TODO If not for limited timeframe, hardcoding should be migrated to JSON-files
    private void drawTutorialText(SpriteBatch batch, int step) {
        font.getData().setScale(0.65f);
        float lineHeight = font.getLineHeight();

        font.draw(batch, "TIP: You can also shuffle through ", Rngg.WIDTH / 32f, Rngg.HEIGHT / 12f);
        font.draw(batch, "the steps, using these buttons.", Rngg.WIDTH / 32f, Rngg.HEIGHT / 12f - lineHeight);

        switch (step) {
            case 1: {
                font.setColor(Color.WHITE);
                font.draw(batch, "This is a map of zones of different colors.", textBeginX, textBeginY);
                font.draw(batch, "You play as the ", textBeginX, textBeginY - lineHeight);
                font.setColor(Color.RED);
                font.draw(batch, "RED Player", textBeginX + 180, textBeginY - lineHeight);
                font.setColor(Color.WHITE);
                font.draw(batch, "You can change this in the settings later.", textBeginX, textBeginY - 2 * lineHeight);
                font.draw(batch, "It's your turn! Attack the top green zone", textBeginX, textBeginY - 4 * lineHeight);
                font.draw(batch, "Since it's only neighbor is your top zone, ", textBeginX, textBeginY - 5 * lineHeight);
                font.draw(batch, "touch that one.", textBeginX, textBeginY - 6 * lineHeight);
                font.draw(batch, "Note that the numbers represent the", textBeginX, textBeginY - 8 * lineHeight);
                font.draw(batch, "number  of dice per zone.", textBeginX, textBeginY - 9 * lineHeight);
                break;
            }
            case 2: {
                font.draw(batch, "Good! Now you have selected a zone.", textBeginX, textBeginY);
                font.draw(batch, "You can now attack neighboring zones.", textBeginX, textBeginY - lineHeight);
                font.draw(batch, "Attack the green zone on top, with 5 dice on it.", textBeginX, textBeginY - 2 * lineHeight);
                break;
            }
            case 3: {
                font.draw(batch, "Good! You captured the zone!", textBeginX, textBeginY);
                font.draw(batch, "As your total score of the dice roll was", textBeginX, textBeginY - lineHeight);
                font.draw(batch, "higher than the opponent's, you won the zone.", textBeginX, textBeginY - 2 * lineHeight);
                font.draw(batch, "Now try to eliminate the green opponent by ", textBeginX, textBeginY - 4 * lineHeight);
                font.draw(batch, "capturing his last zone.", textBeginX, textBeginY - 5 * lineHeight);
                font.draw(batch, "Use the zone you've just captured.", textBeginX, textBeginY - 6 * lineHeight);
                break;
            }
            case 5: {
                font.draw(batch, "Great! You've eliminated the", textBeginX, textBeginY);
                font.setColor(Color.GREEN);
                font.draw(batch, "GREEN Player", textBeginX + 310, textBeginY);
                font.setColor(Color.WHITE);
                font.draw(batch, "Now go for the blue opponent!", textBeginX, textBeginY - lineHeight);
                font.draw(batch, "Use the zone you have with the most dice.", textBeginX, textBeginY - 3 * lineHeight);
                font.draw(batch, "This way you'll be better off, when attacking.", textBeginX, textBeginY - 4 * lineHeight);
                break;
            }
            case 6: {
                font.draw(batch, "You can only attack neighboring zones that", textBeginX, textBeginY);
                font.draw(batch, "you do not own yourself.", textBeginX, textBeginY - lineHeight);
                break;
            }
            case 7: {
                font.draw(batch, "One zone left. Go for it!", textBeginX, textBeginY);
                break;
            }
            case 9: {
                font.draw(batch, "Arghh! You lost the battle and most of", textBeginX, textBeginY);
                font.draw(batch, "your dice. You had a much lower chance of", textBeginX, textBeginY - lineHeight);
                font.draw(batch, "winning, because of the dice count difference.", textBeginX, textBeginY - 2 * lineHeight);
                font.draw(batch, "Now end your turn by pressing \"Next step\".", textBeginX, textBeginY - 4 * lineHeight);
                break;
            }
            case 10: {
                font.draw(batch, "Now it's the turn of the ", textBeginX, textBeginY);
                font.setColor(Color.BLUE);
                font.draw(batch, "BLUE Player", textBeginX + 250, textBeginY);
                font.setColor(Color.WHITE);
                font.draw(batch, "Notice how your dice count increased at", textBeginX, textBeginY - lineHeight);
                font.draw(batch, "the end of your turn!", textBeginX, textBeginY - 2 * lineHeight);
                font.draw(batch, "In a real game you would choose either to", textBeginX, textBeginY - 4 * lineHeight);
                font.draw(batch, "defend your frontier, core or all zones to", textBeginX, textBeginY - 5 * lineHeight);
                font.draw(batch, "manipulate which zones gets new dice.", textBeginX, textBeginY - 6 * lineHeight);
                font.draw(batch, "By clicking the \"Next step\" button, you chose", textBeginX, textBeginY - 8 * lineHeight);
                font.draw(batch, "to defend all zones.", textBeginX, textBeginY - 9 * lineHeight);
                break;
            }
            case 11: {
                font.draw(batch, "Your opponent started by capturing one", textBeginX, textBeginY);
                font.draw(batch, "of your zones.", textBeginX, textBeginY - lineHeight);
                font.draw(batch, "He would most likely go on to capture many", textBeginX, textBeginY - 3 * lineHeight);
                font.draw(batch, "more of your zones, but this is enough for", textBeginX, textBeginY - 4 * lineHeight);
                font.draw(batch, "a simple demonstration.", textBeginX, textBeginY - 5 * lineHeight);
                font.draw(batch, "You should now know enough to play a", textBeginX, textBeginY - 7 * lineHeight);
                font.draw(batch, "game for real.", textBeginX, textBeginY - 8 * lineHeight);
                font.draw(batch, "Quit the tutorial, when ready.", textBeginX, textBeginY - 9 * lineHeight);
                break;
            }
        }
    }
}
