package com.rngg.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rngg.game.Rngg;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Rngg.WIDTH;
        config.height = Rngg.HEIGHT;
		new LwjglApplication(new Rngg(null), config);
	}
}
