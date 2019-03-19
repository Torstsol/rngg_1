package com.rngg.configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

public class GamePreferences {

    private Preferences prefs;
    private ArrayList<Float> colorArray;
    private ArrayList<Color> list;

    public static Color COLOR1, COLOR2, COLOR3, COLOR4;

    public GamePreferences(){
        prefs = Gdx.app.getPreferences("game-preferences");
        COLOR1 = getColor("color 1");
        COLOR2 = getColor("color 2");
        COLOR3 = getColor("color 3");
        COLOR4 = getColor("color 4");
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

    public Color getColor(String color){
        colorArray = new ArrayList<Float>();
        for(String c : prefs.getString(color).split(",")) {
            colorArray.add(Float.parseFloat(c) / 255f);
        }

        return new Color(colorArray.get(0), colorArray.get(1), colorArray.get(2),1);
    }

    public ArrayList<Color> getColorArray(){
        list = new ArrayList<Color>();
        list.add(COLOR1);
        list.add(COLOR2);
        list.add(COLOR3);
        list.add(COLOR4);

        return list;
    }

    public boolean hasColors(){
        return prefs.contains("color 1");
    }
}
