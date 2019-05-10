package com.rngg.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.rngg.services.IPlayServices;
import com.rngg.utils.GameAssetManager;
import com.rngg.utils.ScreenManager;
import com.rngg.utils.Utils;

public class Rngg extends Game {

	public static final int HEIGHT = 720;
	public static final int WIDTH = 1280;

	public ScreenManager screenManager;

	public final IPlayServices playServices;

	public static boolean RUN_DESKTOP;

	public Rngg(IPlayServices playServices) {
		this.playServices = playServices;
		if(!RUN_DESKTOP){
			playServices.signIn();
		}
	}

	public IPlayServices getAPI(){
		return playServices;
	}

	@Override
	public void create () {
        Gdx.app.setLogLevel(Application.LOG_INFO);

		GameAssetManager gameAssetManager = GameAssetManager.getInstance();
        gameAssetManager.loadImages();
        gameAssetManager.loadFonts();
        gameAssetManager.loadSkin();
        gameAssetManager.loadMusic();
        GameAssetManager.getManager().finishLoading();

        screenManager = new ScreenManager(this);
        screenManager.setMenuScreen();
	}

	@Override
	public void render () {
	    screen.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose() {
		super.dispose();

		GameAssetManager.getManager().dispose();
		Utils.dispose();
	}

}
