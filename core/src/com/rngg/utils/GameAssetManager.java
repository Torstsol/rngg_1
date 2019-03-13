package com.rngg.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {

    public final AssetManager manager = new AssetManager();

    public void loadImages() {
        // TODO: add images here
    }

    public void loadFonts() {
        manager.load(Assets.MINECRAFTIA);
    }

    /*
    public void loadSkin() {
        // TODO: implement skin here
        SkinParameter params = new SkinParameter(skinAtlasPath);
        manager.load(skinJsonPath, Skin.class, params);
    }
    */

}
