/*
 * View for the settings screen. Renders buttons which let the user toggle different
 * settings such as music and colors.
 */

package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rngg.configuration.GamePreferences;
import com.rngg.controllers.SettingsController;
import com.rngg.game.Rngg;
import com.rngg.models.SettingsModel;
import com.rngg.utils.Assets;

public class SettingsView extends View {

    SettingsController controller;

    private ShapeRenderer shapeRenderer;

    private ShapeRenderer.ShapeType shapeType;

    private GamePreferences pref;

    private Stage stage;

    private SettingsModel settingsModel;

    private TextButton cbSettingsButton;
    private TextButton color1Button;
    private TextButton color2Button;
    private TextButton color3Button;
    private TextButton color4Button;

    private final SelectBox<String> customMaps;


    public SettingsView(SettingsController controller) {
        this.controller = controller;

        shapeRenderer = new ShapeRenderer();

        shapeType = ShapeRenderer.ShapeType.Filled;

        font.setColor(Color.WHITE);

        pref = GamePreferences.getInstance();

        Skin skin = assetManager.get(Assets.SKIN);

        FitViewport fitViewport = new FitViewport(Rngg.WIDTH, Rngg.HEIGHT, camera);
        stage = new Stage(fitViewport, batch);
        controller.setInputProcessor(stage);

        Table table = new Table();
        table.defaults().pad(10F);
        table.setFillParent(true);

        Label label = new Label("Game Settings", skin);
        label.setAlignment(Align.center);

        Table first_table = new Table();

        Table color_choose_table = new Table();
        color_choose_table.defaults().pad(10F);
        color_choose_table.add(new Label("Choose your color", skin)).row();

        Table game_settings_table = new Table();
        game_settings_table.defaults().pad(10F);
        game_settings_table.defaults().padLeft(50F);
        game_settings_table.add(new Label("Choose your in-game settings", skin)).row();

        Table second_table = new Table();
        second_table.defaults().pad(10F);
        second_table.row();

        Table third_table = new Table();
        third_table.defaults().pad(10F);
        third_table.row();

        table.add(label).colspan(2).fillX().row();
        first_table.add(color_choose_table).expand();
        first_table.add(game_settings_table).expand().row();
        table.add(first_table).expand().row();
        table.add(second_table).expand().row();
        table.add(third_table).expand().row();

        VerticalGroup group1 = new VerticalGroup();
        group1.grow();
        group1.space(30);
        color_choose_table.add(group1).width(300);

        VerticalGroup group2 = new VerticalGroup();
        group2.grow();
        group2.space(30);
        second_table.add(group2).width(500);

        VerticalGroup group3 = new VerticalGroup();
        group3.grow();
        third_table.add(group3);

        VerticalGroup group4 = new VerticalGroup();
        group4.grow();
        group4.space(30);
        game_settings_table.add(group4).width(400);

        HorizontalGroup customMapGroup = new HorizontalGroup();
        customMapGroup.grow();
        customMapGroup.space(30);

        stage.addActor(table);

        settingsModel = new SettingsModel();

        color1Button = new TextButton("Color 1", skin);
        color1Button.getLabel().setFontScale(1.5f);
        group1.addActor(color1Button);

        color2Button = new TextButton("Color 2", skin);
        color2Button.getLabel().setFontScale(1.5f);
        group1.addActor(color2Button);

        color3Button = new TextButton("Color 3", skin);
        color3Button.getLabel().setFontScale(1.5f);
        group1.addActor(color3Button);

        color4Button = new TextButton("Color 4", skin);
        color4Button.getLabel().setFontScale(1.5f);
        group1.addActor(color4Button);

        final TextButton mapButton = new TextButton(settingsModel.getMapButtonText(), skin);
        mapButton.getLabel().setFontScale(1.5f);
        customMapGroup.addActor(mapButton);

        customMaps = new SelectBox<String>(skin);
        customMaps.getStyle().font.getData().setScale(3f);
        customMapGroup.addActor(customMaps);

        group4.addActor(customMapGroup);

        final TextButton diceBytton = new TextButton(pref.getDiceType(), skin);
        diceBytton.getLabel().setFontScale(1.5f);
        group4.addActor(diceBytton);

        final Slider diceNumSLider = settingsModel.createSlider(skin);
        Container<Slider> container = new Container<Slider>(diceNumSLider);
        container.setTransform(true);   // for enabling scaling and rotation
        container.size(200, 10);
        container.setScale(2);  //scale according to your requirement
        Label sliderLabel = new Label("Number of dices: " + pref.getNumDice(), skin);
        sliderLabel.setAlignment(Align.center);
        group4.addActor(sliderLabel);
        group4.addActor(container);

        cbSettingsButton = new TextButton("Colorblind mode [" + (pref.getCbMode() ? "enabled" : "disabled") + "]", skin);
        cbSettingsButton.getLabel().setFontScale(1.5f);
        group2.addActor(cbSettingsButton);

        final TextButton toggleMusic = new TextButton("Music [" + (pref.isMusicEnabled() ? "enabled" : "disabled") + "]", skin);
        toggleMusic.getLabel().setFontScale(1.5f);
        group2.addActor(toggleMusic);

        final TextButton menuButton = new TextButton("Back", assetManager.get(Assets.SKIN));
        menuButton.getLabel().setFontScale(1.5f);
        group3.addActor(menuButton);


        controller.addActorListeners(cbSettingsButton, toggleMusic, menuButton, color1Button, color2Button, color3Button, color4Button, mapButton, diceBytton, diceNumSLider, sliderLabel, customMaps); // handle input
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(shapeType);
        settingsModel.setCustomColor("color 1", shapeRenderer, 100, Rngg.HEIGHT / 2 + 225);
        settingsModel.setCustomColor("color 2", shapeRenderer, 100, Rngg.HEIGHT / 2 + 75);
        settingsModel.setCustomColor("color 3", shapeRenderer, 100, Rngg.HEIGHT / 2 - 75);
        settingsModel.setCustomColor("color 4", shapeRenderer, 100, Rngg.HEIGHT / 2 - 225);
        shapeRenderer.end();

        int x_margin = 92;

        batch.begin();
        settingsModel.drawText(font, batch, Integer.toString(1), x_margin, Rngg.HEIGHT / 2 + 235);
        settingsModel.drawText(font, batch, Integer.toString(2), x_margin, Rngg.HEIGHT / 2 + 85);
        settingsModel.drawText(font, batch, Integer.toString(3), x_margin, Rngg.HEIGHT / 2 - 65);
        settingsModel.drawText(font, batch, Integer.toString(4), x_margin, Rngg.HEIGHT / 2 - 215);
        batch.end();

        customMaps.setColor(pref.customMapEnabled() ? Color.WHITE : Color.CLEAR);
        customMaps.setDisabled(!pref.customMapEnabled());
        customMaps.setItems(pref.customMapEnabled() ? settingsModel.getCustomMaps() : new Array<String>());
        customMaps.pack();

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
