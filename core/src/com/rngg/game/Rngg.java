package com.rngg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.rngg.controllers.MenuController;
import com.rngg.services.IPlayServices;
import com.rngg.views.MenuView;

public class Rngg extends Game {

	public static final int HEIGHT = 720;
	public static final int WIDTH = 1280;

	public final IPlayServices playServices;

	public Rngg(IPlayServices playServices) {
		this.playServices = playServices;
		playServices.signIn();
	}

	public IPlayServices getAPI(){

		return playServices;
	}

	@Override
	public void create () {
        Gdx.app.setLogLevel(Application.LOG_INFO);
		this.setScreen(new MenuView(new MenuController(this)));
	}

	@Override
	public void render () {
	    screen.render(Gdx.graphics.getDeltaTime());
	}

}
