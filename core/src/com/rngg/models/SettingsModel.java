package com.rngg.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.Array;
import com.rngg.configuration.GamePreferences;

public class SettingsModel {

    private GamePreferences pref;

    public SettingsModel() {

        pref = GamePreferences.getInstance();

    }

    public void setCustomColor(String color, ShapeRenderer sr, int x, int y) {
        /*
        Pixmap pmap = new Pixmap(100,100, Pixmap.Format.RGB888);
        pmap.setColor(pref.getColor(color));
        pmap.fill();

        return new Texture(pmap);
        */

        sr.setColor(pref.getColor(color));
        sr.circle(x, y, 55);

    }

    public void drawText(BitmapFont font, SpriteBatch sb, String text, int x, int y) {
        if (text.equals(pref.getMainColorString().split(" ")[1])) {
            font.setColor(Color.BLACK);
        } else {
            font.setColor(Color.WHITE);
        }
        font.draw(sb, text, x, y);
    }

    public Slider createSlider(Skin skin) {
        int length = pref.getNumDice();
        Slider slider = new Slider(1, 8, 1, false, skin);
        float[] sliderValues = new float[length];
        for (int i = 0; i < length; i++) {
            sliderValues[i] = i + 1;
        }
        slider.setValue(pref.getNumDice());
        return slider;
    }

    public String getMapButtonText() {
        String map = pref.getMapType();
        if (map.equals("SquareMap")) {
            return "SquareMap";
        } else if (map.equals("HexMap")) {
            return "Hexagonal map";
        } else if (map.equals("HexMeshMap")) {
            return "HexMeshMap";
        }

        return "Custom Map";
    }

    public Array<String> getCustomMaps() {
        FileHandle dirHandle = Gdx.files.internal("levels/");
        Array<String> mapNames = new Array<String>();

        for (FileHandle entry : dirHandle.list()) {
            if (entry.extension().equals("json")) mapNames.add(entry.nameWithoutExtension());
        }

        return mapNames;
    }
}
