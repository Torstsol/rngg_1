package com.rngg.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.rngg.controllers.MenuController;
import com.rngg.services.API;
import com.rngg.views.MenuView;

public class Rngg extends Game {

	public final API api;

	public Rngg(API api) {
		this.api = api;
	}

	public API getAPI(){
		return api;
	}

	@Override
	public void create () {
		this.setScreen(new MenuView(new MenuController(this)));
	}

	@Override
	public void render () {
	    screen.render(Gdx.graphics.getDeltaTime());
	}

}
