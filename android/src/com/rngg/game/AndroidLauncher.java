package com.rngg.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


import GmsServices.AndroidAPI;
import de.golfgl.gdxgamesvcs.GpgsClient;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Rngg game = new Rngg();
		game.gsClient = new GpgsClient().initialize(this, false);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(game, config);
	}
}
