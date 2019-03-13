package com.rngg.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.rngg.services.IPlayServices;
import com.rngg.utils.GameAssetManager;
import com.rngg.utils.ScreenManager;

public class Rngg extends Game {

	public static final int HEIGHT = 720;
	public static final int WIDTH = 1280;

	public GameAssetManager assetManager;
	public ScreenManager screenManager;

	public final IPlayServices playServices;

	public Rngg(IPlayServices playServices) {
		this.playServices = playServices;
		//playServices.signIn();
	}

	public IPlayServices getAPI(){
		return playServices;
	}

	@Override
	public void create () {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        assetManager = new GameAssetManager();
        assetManager.loadImages();
        assetManager.loadFonts();
        assetManager.manager.finishLoading();

        screenManager = new ScreenManager(this);
        screenManager.setMenuScreen();
	}

	@Override
	public void render () {
	    screen.render(Gdx.graphics.getDeltaTime());
	}

}
