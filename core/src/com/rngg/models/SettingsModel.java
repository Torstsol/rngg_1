package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.rngg.configuration.GamePreferences;

import java.util.ArrayList;

public class SettingsModel {

    private GamePreferences pref;
    private Texture pmapTexture;
    private ArrayList<Float> colorArray;
    private Drawable drawable;
    private ImageButton colorButton;

    public SettingsModel(){

        pref = GamePreferences.getInstance();

    }

    public Texture setCustomColor(String color){

        Pixmap pmap = new Pixmap(100,100, Pixmap.Format.RGB888);
        pmap.setColor(pref.getColor(color));
        pmap.fill();

        return new Texture(pmap);
    }

}
