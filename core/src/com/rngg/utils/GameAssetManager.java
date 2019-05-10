/*
 * This class is a singleton wrapper for LibGDXs AssetManager class.
 * In addition it has methods for loading groups of assets.
 */

package com.rngg.utils;

import com.badlogic.gdx.assets.AssetManager;

public class GameAssetManager {

    // since this is a singleton, prevent creation of new objects
    private GameAssetManager() {

    }

    private static final GameAssetManager instance = new GameAssetManager();

    private final AssetManager manager = new AssetManager();

    public void loadImages() {
        manager.load(Assets.LOGO);
    }

    public void loadFonts() {
        manager.load(Assets.FONT);
    }

    public void loadSkin() {
        manager.load(Assets.SKIN);
    }

    public void loadMusic() {
        manager.load(Assets.MUSIC);
    }

    public static GameAssetManager getInstance() {
        return instance;
    }

    public static AssetManager getManager() {
        return instance.manager;
    }

}
