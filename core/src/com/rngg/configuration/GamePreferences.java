package com.rngg.configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences {

    private Preferences prefs;

    public GamePreferences(){
        prefs = Gdx.app.getPreferences("game-preferences");
    }

    public void setTrueColors(){
        this.setColors("213,0,0", "240,228,66", "0,114,178", "86,180,233");
        prefs.flush();
    }

    public void setColors(String c1, String c2, String c3, String c4){
        prefs.putString("color 1", c1);
        prefs.putString("color 2", c2);
        prefs.putString("color 3", c3);
        prefs.putString("color 4", c4);
        prefs.flush();
    }

    public boolean hasColors(){
        return prefs.contains("color 1");
    }
}
