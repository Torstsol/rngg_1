package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rngg.controllers.LobbyController;
import com.rngg.game.Rngg;
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

        FitViewport fitViewport = new FitViewport(Rngg.WIDTH, Rngg.HEIGHT, camera);
        stage = new Stage(fitViewport, batch);
        controller.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        VerticalGroup group = new VerticalGroup();
        group.grow();
        group.space(8);
        table.add(group);

        stage.addActor(table);


        final TextButton quickGameButton = new TextButton("Quick Game", assetManager.manager.get(Assets.SKIN));
        group.addActor(quickGameButton);

        final TextButton invitePlayersButton = new TextButton("Invite Players", assetManager.manager.get(Assets.SKIN));
        group.addActor(invitePlayersButton);

        final TextButton localGameButton = new TextButton("Local Game", assetManager.manager.get(Assets.SKIN));
        group.addActor(localGameButton);

        final TextButton menuButton = new TextButton("Cancel", assetManager.manager.get(Assets.SKIN));
        group.addActor(menuButton);

        controller.addActorListeners(quickGameButton, invitePlayersButton, localGameButton, menuButton); // handle input
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
