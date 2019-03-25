package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.rngg.configuration.GamePreferences;
import com.rngg.controllers.SettingsController;
import com.rngg.models.SettingsModel;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;

public class SettingsView extends View {

    SettingsController controller;

    private GamePreferences pref;

    private SpriteBatch batch;

    private Stage stage;

    private SettingsModel settingsModel;

    private TextButton cbSettingsButton;

    public SettingsView(GameAssetManager assetManager, SettingsController controller) {
        super(assetManager);

        this.controller = controller;

        pref = new GamePreferences();

        batch = new SpriteBatch();

        stage = new Stage();
        controller.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        VerticalGroup group = new VerticalGroup();
        group.grow();
        group.space(8);
        table.add(group).width(500);

        stage.addActor(table);

        settingsModel = new SettingsModel();

        cbSettingsButton = new TextButton("Colorblind mode [" + pref.getCbModeString() + "]", assetManager.manager.get(Assets.SKIN));
        group.addActor(cbSettingsButton);

        final TextButton menuButton = new TextButton("Cancel", assetManager.manager.get(Assets.SKIN));
        group.addActor(menuButton);

        controller.addActorListeners(cbSettingsButton, menuButton); // handle input
    }

    @Override
    public void render(float delta) {
        controller.update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(settingsModel.setCustomColor("color 1"),150,450);
        batch.draw(settingsModel.setCustomColor("color 2"),150,350);
        batch.draw(settingsModel.setCustomColor("color 3"),150,250);
        batch.draw(settingsModel.setCustomColor("color 4"),150,150);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
