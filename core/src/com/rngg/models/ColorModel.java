package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class ColorModel {

    Preferences prefs;
    private Texture pmapTexture1;
    private Texture pmapTexture2;
    private Texture pmapTexture3;
    private Texture pmapTexture4;
    private ArrayList<Float> colorArray;

    public ColorModel(){

        prefs = Gdx.app.getPreferences("color-preferences");

    }

    public void draw(SpriteBatch sb) {

        pmapTexture1 = this.setCustomColor("red");
        pmapTexture2 = this.setCustomColor("yellow");
        pmapTexture3 = this.setCustomColor("blue");
        pmapTexture4 = this.setCustomColor("sky blue");

        sb.draw(pmapTexture1,50,350);
        sb.draw(pmapTexture2,150,350);
        sb.draw(pmapTexture3,250,350);
        sb.draw(pmapTexture4,350,350);

    }

    private Texture setCustomColor(String color){
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
