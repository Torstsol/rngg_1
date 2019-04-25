package com.rngg.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.rngg.services.IPlayServices;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;
import com.rngg.utils.ScreenManager;

public class Rngg extends Game {

	public static final int HEIGHT = 720;
	public static final int WIDTH = 1280;

	public GameAssetManager assetManager;
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

        assetManager = GameAssetManager.getInstance();
        assetManager.loadImages();
        assetManager.loadFonts();
        assetManager.loadSkin();
        //assetManager.loadMusic();
        assetManager.loadSounds();
        assetManager.manager.finishLoading();

		if(assetManager.manager.isLoaded(Assets.MUSIC)) {
			assetManager.manager.get(Assets.MUSIC).play();
		}

        screenManager = new ScreenManager(this);
        screenManager.setMenuScreen();
	}

	@Override
	public void render () {
	    screen.render(Gdx.graphics.getDeltaTime());
	}

}
