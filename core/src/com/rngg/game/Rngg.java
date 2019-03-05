package com.rngg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.views.MainMenuView;

public class Rngg extends Game {
	@Override
	public void create () {
		this.setScreen(new MainMenuView(this));
	}

	@Override
	public void render () {
	    screen.render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {

	}
}
