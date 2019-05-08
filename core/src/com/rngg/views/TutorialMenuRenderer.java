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
    private ShapeRenderer shapeRenderer;
    private ShapeRenderer.ShapeType shapeType;
    private final TextButton prevButton;
    private final TextButton nextButton;
    private final TextButton quitButton;
    private float timeCounter;

    public TutorialMenuRenderer(TutorialModel model, BitmapFont font, TutorialController controller, ShapeRenderer shapeRenderer) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Rngg.WIDTH, Rngg.HEIGHT);
        batch = Utils.getSpriteBatch();
        this.font = font;
        this.model = model;
        this.shapeRenderer = shapeRenderer;
        this.shapeType = ShapeRenderer.ShapeType.Filled;
        this.prevButton = new TextButton("Prev step", GameAssetManager.getManager().get(Assets.SKIN));
        this.nextButton = new TextButton("Next step", GameAssetManager.getManager().get(Assets.SKIN));
        this.quitButton = new TextButton("Quit tutorial", GameAssetManager.getManager().get(Assets.SKIN));
        this.timeCounter = Gdx.graphics.getDeltaTime();

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
        group.padTop((float) Rngg.HEIGHT * 8 / 9); // Ghetto positioning

        this.controller = controller;
        this.controller.addInputProcessor(stage);
        this.controller.addMenuActorListeners(prevButton, nextButton, quitButton);
    }

    public void draw() {
        prevButton.setTouchable(model.hasPrev() ? Touchable.enabled : Touchable.disabled);
        prevButton.setColor(model.hasPrev() ? Color.WHITE  : Color.GRAY);
        nextButton.setTouchable(model.hasNext() ? Touchable.enabled : Touchable.disabled);
        nextButton.setColor(model.hasNext() ? Color.WHITE : Color.GRAY);

        int step = model.getStep();
        if (step > 8) {
            float dt = Gdx.graphics.getDeltaTime();
            timeCounter += dt;

            if (timeCounter > 50 * dt) {
                ((step == 9) ? nextButton : quitButton).setColor(Color.RED);
            }
            if (timeCounter > 100 * dt) {
                ((step == 9) ? nextButton : quitButton).setColor(Color.WHITE);
                timeCounter = 0f;
            }
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // TODO Fill with tutorial text
        batch.end();
        stage.draw();
    }

}
