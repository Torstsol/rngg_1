package com.rngg.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.rngg.controllers.MenuController;
import com.rngg.services.API;
import com.rngg.views.MenuView;

import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.NoGameServiceClient;

public class Rngg extends Game {

	public IGameServiceClient gsClient;

	public Rngg() { }

	@Override
	public void create () {
		if (gsClient == null)
			gsClient = new NoGameServiceClient();

		// for getting callbacks from the client
		gsClient.setListener(Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				
			}
		}));

		// establish a connection to the game service without error messages or login screens
		gsClient.resumeSession();

		this.setScreen(new MenuView(new MenuController(this)));
	}

	@Override
	public void pause() {
		super.pause();

		gsClient.pauseSession();
	}

	@Override
	public void resume() {
		super.resume();

		gsClient.resumeSession();
	}

	@Override
	public void render () {
	    screen.render(Gdx.graphics.getDeltaTime());
	}

}
