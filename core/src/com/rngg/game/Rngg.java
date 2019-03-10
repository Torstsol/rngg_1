package com.rngg.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.rngg.controllers.MenuController;
import com.rngg.views.MenuView;

public class Rngg extends Game {

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
