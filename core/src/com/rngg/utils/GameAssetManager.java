package com.rngg.utils;

import com.badlogic.gdx.assets.AssetManager;

public class GameAssetManager {

    private static final GameAssetManager instance = new GameAssetManager();

    private GameAssetManager() {

    }

    public final AssetManager manager = new AssetManager();

    public void loadImages() {
        manager.load(Assets.LOGO);
    }

    public void loadFonts() {
        manager.load(Assets.MINECRAFTIA);
    }

    public void loadSkin() {
        manager.load(Assets.SKIN);
    }

    public void loadMusic() {
        manager.load(Assets.MUSIC);
    }

    public void loadSounds() {
        manager.load(Assets.OOF);
    }

    public static GameAssetManager getInstance() {
        return instance;
    }

}
