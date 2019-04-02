package com.rngg.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

    public static final AssetDescriptor<BitmapFont>
            MINECRAFTIA = new AssetDescriptor("fonts/yaheilight.fnt", BitmapFont.class);

    public static final AssetDescriptor<Skin> SKIN = new AssetDescriptor("skins/flat-earth/skin/flat-earth-ui.json", Skin.class);

    public static final AssetDescriptor<Texture> LOGO = new AssetDescriptor("images/logo.png", Texture.class);

}
