package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.rngg.controllers.LobbyController;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;

public class LobbyView extends View {

    LobbyController controller;

    private SpriteBatch batch;

    private Stage stage;

    public LobbyView(GameAssetManager assetManager, LobbyController controller) {
        super(assetManager);

        this.controller = controller;

        batch = new SpriteBatch();

        stage = new Stage();
        controller.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        VerticalGroup group = new VerticalGroup();
        group.grow();
        group.space(8);
        table.add(group);

        stage.addActor(table);


        final TextButton gameButton = new TextButton("Play", assetManager.manager.get(Assets.SKIN));
        group.addActor(gameButton);

        final TextButton menuButton = new TextButton("Cancel", assetManager.manager.get(Assets.SKIN));
        group.addActor(menuButton);

        controller.addActorListeners(gameButton, menuButton); // handle input
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
