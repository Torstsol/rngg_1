package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rngg.controllers.MenuController;
import com.rngg.game.Rngg;
import com.rngg.utils.Assets;

public class MenuView extends View {

    MenuController controller;

    private Stage stage;

    public MenuView(MenuController controller) {
        this.controller = controller;

        FitViewport fitViewport = new FitViewport(Rngg.WIDTH, Rngg.HEIGHT, camera);
        stage = new Stage(fitViewport, batch);
        controller.setInputProcessor(stage);

        Table table = new Table();
        table.defaults().pad(40f);
        table.setFillParent(true);

        Table imageTable = new Table();
        imageTable.row();
        //imageTable.setFillParent(true);

        Table buttonTable = new Table();
        buttonTable.row();
        //buttonTable.setFillParent(true);

        table.add(imageTable);
        table.add(buttonTable);

        VerticalGroup imageGroup = new VerticalGroup();
        imageGroup.grow();
        imageTable.add(imageGroup);

        VerticalGroup buttonGroup = new VerticalGroup();
        buttonGroup.grow();
        buttonGroup.space(50);
        buttonTable.add(buttonGroup).width(300);

        stage.addActor(table);

        final Texture logoTexture = assetManager.get(Assets.LOGO);
        final Image logoImage = new Image(logoTexture);
        //logoImage.setOrigin(Rngg.WIDTH/2, Rngg.HEIGHT/2);

        imageGroup.addActor(logoImage);

        final Label label = new Label("Are you ready to expand your empire?", assetManager.get(Assets.SKIN));
        buttonGroup.addActor(label);

        final TextButton lobbyButton = new TextButton("Play", assetManager.get(Assets.SKIN));
        lobbyButton.getLabel().setFontScale(2f);
        buttonGroup.addActor(lobbyButton);

        final TextButton tutorialButton = new TextButton("Tutorial", assetManager.get(Assets.SKIN));
        tutorialButton.getLabel().setFontScale(2f);
        buttonGroup.addActor(tutorialButton);

        final TextButton settingsButton = new TextButton("Settings", assetManager.get(Assets.SKIN));
        settingsButton.getLabel().setFontScale(2f);
        buttonGroup.addActor(settingsButton);

        final TextButton exitButton = new TextButton("Exit", assetManager.get(Assets.SKIN));
        exitButton.getLabel().setFontScale(2f);
        buttonGroup.addActor(exitButton);

        controller.addActorListeners(lobbyButton, tutorialButton, settingsButton, exitButton); // handle input
    }

    @Override
    public void show() {
        // TODO: If this class becomes a singleton, set inputprocessor here
    }

    @Override
    public void render(float delta) {
        controller.update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
