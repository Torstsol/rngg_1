package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.controllers.ColorController;
import com.rngg.controllers.MenuController;

import java.awt.Color;
import java.awt.image.ColorConvertOp;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ColorView extends View {

    ColorController controller;

    private SpriteBatch batch;
    private BitmapFont font;
    Preferences prefs;
    private Texture pmapTexture1;
    private Texture pmapTexture2;
    private Texture pmapTexture3;
    private Texture pmapTexture4;
    private ArrayList<Integer> colorArray;

    public ColorView(ColorController controller) {
        this.controller = controller;

        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("minecraftia.fnt"),
                Gdx.files.internal("minecraftia.png"), false);


        prefs = Gdx.app.getPreferences("color-preferences");
        pmapTexture1 = this.setCustomColor("red");
        pmapTexture2 = this.setCustomColor("yellow");
        pmapTexture3 = this.setCustomColor("blue");
        pmapTexture4 = this.setCustomColor("sky blue");

    }

    @Override
    public void render(float delta) {
        controller.update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, ">Color Settings View<", 50, 250);
        font.draw(batch, "Press 'b' to go back", 50, 150);
        batch.draw(pmapTexture1,50,350);
        batch.draw(pmapTexture2,150,350);
        batch.draw(pmapTexture3,250,350);
        batch.draw(pmapTexture4,350,350);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();


    }

    private Texture setCustomColor(String color){
        colorArray = new ArrayList<Integer>();
        for(String c : prefs.getString(color).split(",")) {
            colorArray.add(Integer.parseInt(c));
        }

        Pixmap pmap = new Pixmap(100,100, Pixmap.Format.RGB888);
        pmap.setColor(colorArray.get(0), colorArray.get(1), colorArray.get(2),1);
        pmap.fill();

        return new Texture(pmap);
    }


}
