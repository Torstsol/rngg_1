package com.rngg.utils.hex;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Utils {

    private static SpriteBatch spriteBatch = new SpriteBatch();

    public static SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void dispose() {
        spriteBatch.dispose();
    }

}
