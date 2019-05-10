/*
 * A class meant to collect paths to assets together,
 * to allow for easier modification.
 */

package com.rngg.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

    // fonts
    public static final AssetDescriptor<BitmapFont>
            FONT = new AssetDescriptor("fonts/yaheilight.fnt", BitmapFont.class);

    // skins
    public static final AssetDescriptor<Skin> SKIN = new AssetDescriptor("skins/flat-earth/skin/flat-earth-ui.json", Skin.class);

    // textures
    public static final AssetDescriptor<Texture> LOGO = new AssetDescriptor("images/logo.png", Texture.class);

    // music
    public static final AssetDescriptor<Music> MUSIC = new AssetDescriptor("audio/menu-music.mp3", Music.class);

}
