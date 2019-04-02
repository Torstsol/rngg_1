package com.rngg.utils;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

    public static final AssetDescriptor<BitmapFont>
            MINECRAFTIA = new AssetDescriptor("fonts/minecraftia.fnt", BitmapFont.class);

    public static final AssetDescriptor<Skin> SKIN = new AssetDescriptor("skin/uiskin.json", Skin.class);

    public static final AssetDescriptor<Texture> LOGO = new AssetDescriptor("images/logo.png", Texture.class);

    public static final AssetDescriptor<Music> MUSIC = new AssetDescriptor("audio/menu-music.mp3", Music.class);

    public static final AssetDescriptor<Sound> OOF = new AssetDescriptor<Sound>("audio/oof.mp3", Sound.class);

}
