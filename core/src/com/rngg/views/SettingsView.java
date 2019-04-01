package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

    protected BitmapFont font;

    private SpriteBatch batch;

    private Stage stage;

    private SettingsModel settingsModel;

    private TextButton cbSettingsButton;

    public SettingsView(GameAssetManager assetManager, SettingsController controller) {
        super(assetManager);

        this.controller = controller;

        font = assetManager.manager.get(Assets.MINECRAFTIA);
        font.setColor(Color.WHITE);

        pref = GamePreferences.getInstance();

        batch = new SpriteBatch();

        stage = new Stage();
        controller.setInputProcessor(stage);

        Table table1 = new Table();
        table1.setFillParent(true);

        Table table2 = new Table();
        table2.setFillParent(true);

        VerticalGroup group1 = new VerticalGroup();
        group1.grow();
        group1.space(8);
        table1.add(group1).width(500);

        VerticalGroup group2 = new VerticalGroup();
        group2.grow();
        group2.space(8);
        table2.add(group2).width(300);

        stage.addActor(table1);
        stage.addActor(table2);

        settingsModel = new SettingsModel();

        cbSettingsButton = new TextButton("Colorblind mode [" + pref.getCbModeString() + "]", assetManager.manager.get(Assets.SKIN));
        group1.addActor(cbSettingsButton);

        final TextButton menuButton = new TextButton("Cancel", assetManager.manager.get(Assets.SKIN));
        group1.addActor(menuButton);

        final TextButton color1Button = new TextButton("Color 1", assetManager.manager.get(Assets.SKIN));
        group2.addActor(color1Button);

        final TextButton color2Button = new TextButton("Color 2", assetManager.manager.get(Assets.SKIN));
        group2.addActor(color2Button);

        final TextButton color3Button = new TextButton("Color 3", assetManager.manager.get(Assets.SKIN));
        group2.addActor(color3Button);

        final TextButton color4Button = new TextButton("Color 4", assetManager.manager.get(Assets.SKIN));
        group2.addActor(color4Button);


        controller.addActorListeners(cbSettingsButton, menuButton, color1Button, color2Button, color3Button, color4Button); // handle input
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
        font.draw(batch, Integer.toString(1), 150, 450);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
