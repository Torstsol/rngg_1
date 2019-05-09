/*
 * This is the entry for our game. It sets up necessary resources and configurations,
 * and starts loading of assets.
 * After assets have loaded the menu screen is set as the first screen.
 */

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
        if (!RUN_DESKTOP) {
            playServices.signIn();
        }
    }

    public IPlayServices getAPI() {
        return playServices;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        // setup asset manager and load assets
        GameAssetManager gameAssetManager = GameAssetManager.getInstance();
        gameAssetManager.loadImages();
        gameAssetManager.loadFonts();
        gameAssetManager.loadSkin();
        gameAssetManager.loadMusic();
        GameAssetManager.getManager().finishLoading();

        // setup screen manager and set menu screen
        screenManager = new ScreenManager(this);
        screenManager.setMenuScreen();
    }

    @Override
    public void render() {
        // render current screen
        screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        super.dispose();

        // dispose all disposables to free up memory
        GameAssetManager.getManager().dispose();
        Utils.dispose();
    }

}
