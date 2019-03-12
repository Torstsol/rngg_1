package com.rngg.configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ColorPrefs {

    private Preferences prefs;

    public ColorPrefs(){
        prefs = Gdx.app.getPreferences("color-preferences");
    }

    public void setTrueColors(){
        this.setColors("213,0,0", "240,228,66", "0,114,178", "86,180,233");
        prefs.flush();
    }

    public void setColors(String r, String y, String b, String sb){
        prefs.putString("red", r);
        prefs.putString("yellow", y);
        prefs.putString("blue", b);
        prefs.putString("sky blue", sb);
        prefs.flush();
    }

    public boolean hasColors(){
        return prefs.contains("red");
    }
}
