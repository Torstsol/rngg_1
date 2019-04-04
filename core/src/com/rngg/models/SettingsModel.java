package com.rngg.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rngg.configuration.GamePreferences;

public class SettingsModel {

    private GamePreferences pref;

    public SettingsModel(){

        pref = GamePreferences.getInstance();

    }

    public void setCustomColor(String color, ShapeRenderer sr, int x, int y){
        /*
        Pixmap pmap = new Pixmap(100,100, Pixmap.Format.RGB888);
        pmap.setColor(pref.getColor(color));
        pmap.fill();

        return new Texture(pmap);
        */

        sr.setColor(pref.getColor(color));
        sr.circle(x,y,55);

    }

    public void drawText(BitmapFont font, SpriteBatch sb, String text, int x, int y){
        if(text.equals(pref.getMainColorString().split(" ")[1])){
            font.setColor(Color.BLACK);
        } else {
            font.setColor(Color.WHITE);
        }
        font.draw(sb, text, x, y);
    }

}
