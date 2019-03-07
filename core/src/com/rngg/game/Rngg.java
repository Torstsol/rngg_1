package com.rngg.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.rngg.controllers.MainMenuController;
import com.rngg.views.MainMenuView;

public class Rngg extends Game {
	@Override
	public void create () {
		this.setScreen(new MainMenuView(new MainMenuController(this)));
	}

	@Override
	public void render () {
	    screen.render(Gdx.graphics.getDeltaTime());
	}

}
