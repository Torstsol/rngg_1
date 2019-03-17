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

import java.util.ArrayList;

public class SettingsModel {

    Preferences prefs;
    private Texture pmapTexture;
    private ArrayList<Float> colorArray;
    private Drawable drawable;
    private ImageButton colorButton;

    public SettingsModel(){

        prefs = Gdx.app.getPreferences("color-preferences");

    }

    public Texture setCustomColor(String color){
        colorArray = new ArrayList<Float>();
        for(String c : prefs.getString(color).split(",")) {
            colorArray.add(Float.parseFloat(c) / 255f);
        }

        Pixmap pmap = new Pixmap(100,100, Pixmap.Format.RGB888);
        pmap.setColor(colorArray.get(0), colorArray.get(1), colorArray.get(2),1);
        pmap.fill();

        return new Texture(pmap);
    }

}
