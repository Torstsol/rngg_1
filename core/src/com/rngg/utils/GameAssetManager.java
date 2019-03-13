package com.rngg.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {

    public final AssetManager manager = new AssetManager();

    public void loadImages() {
        // TODO: add images here
    }

    public void loadFonts() {
        manager.load(Assets.MINECRAFTIA);
    }

    public void loadSkin() {
        manager.load(Assets.SKIN);


        // TODO: implement skin here
        /*
        SkinParameter params = new SkinParameter(skinAtlasPath);
        manager.load(Assets.SKIN,  params);
        */
    }

}
