package com.rngg.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rngg.controllers.ColorController;
import com.rngg.models.ColorModel;

public class ColorView extends View {

    ColorController controller;

    private SpriteBatch batch;
    private BitmapFont font;
    private ColorModel colorModel;

    public ColorView(ColorController controller) {
        this.controller = controller;

        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("minecraftia.fnt"),
                Gdx.files.internal("minecraftia.png"), false);

        this.colorModel = new ColorModel();

    }

    @Override
    public void render(float delta) {
        controller.update(delta);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, ">Color Settings View<", 50, 250);
        font.draw(batch, "Protanopia 'p'", 450, 100);
        font.draw(batch, "Deuteranopia 'd'", 450, 150);
        font.draw(batch, "Tritanopia 't'", 50, 100);
        font.draw(batch, "Normal colors 'n'", 50, 150);
        font.draw(batch, "Press 'b' to go back", 50, 50);
        colorModel.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();


    }


}
