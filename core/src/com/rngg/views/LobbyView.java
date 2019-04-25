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

public class LobbyView extends View {

    LobbyController controller;

    private SpriteBatch batch;

    private Stage stage;

    public LobbyView(LobbyController controller) {
        this.controller = controller;

        batch = new SpriteBatch();

        FitViewport fitViewport = new FitViewport(Rngg.WIDTH, Rngg.HEIGHT, camera);
        stage = new Stage(fitViewport, batch);
        controller.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        VerticalGroup group = new VerticalGroup();
        group.grow();
        group.space(30);
        table.add(group);

        stage.addActor(table);


        final TextButton quickGameButton = new TextButton("Quick Game", assetManager.manager.get(Assets.SKIN));
        quickGameButton.getLabel().setFontScale(2f);
        group.addActor(quickGameButton);

        final TextButton invitePlayersButton = new TextButton("Invite Players", assetManager.manager.get(Assets.SKIN));
        invitePlayersButton.getLabel().setFontScale(2f);
        group.addActor(invitePlayersButton);

        final TextButton seeInvitationButton = new TextButton("Invitations", assetManager.manager.get(Assets.SKIN));
        seeInvitationButton.getLabel().setFontScale(2f);
        group.addActor(seeInvitationButton);

        final TextButton localGameButton = new TextButton("Local Game", assetManager.manager.get(Assets.SKIN));
        localGameButton.getLabel().setFontScale(2f);
        group.addActor(localGameButton);

        final TextButton menuButton = new TextButton("Back", assetManager.manager.get(Assets.SKIN));
        menuButton.getLabel().setFontScale(2f);
        group.addActor(menuButton);

        controller.addActorListeners(quickGameButton, invitePlayersButton, seeInvitationButton, localGameButton, menuButton); // handle input
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
