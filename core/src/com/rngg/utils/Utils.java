/*
 * This class collects common used resources which are heavy and are
 * not assets, and may be shared.
 */

package com.rngg.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Utils {

    private static SpriteBatch spriteBatch = new SpriteBatch();

    public static SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public static void dispose() {
        spriteBatch.dispose();
    }

}
