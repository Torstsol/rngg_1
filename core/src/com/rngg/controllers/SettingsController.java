package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rngg.configuration.GamePreferences;
import com.rngg.game.Rngg;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;

public class SettingsController extends Controller {

    private GamePreferences pref;
    private GameAssetManager assetManager;

    public SettingsController(Rngg game) {
        super(game);

        pref = GamePreferences.getInstance();
        assetManager = GameAssetManager.getInstance();
    }

    @Override
    public void update(float delta) {}

    public void addActorListeners(final TextButton cbSettingsButton,
                                  final TextButton musicButton,
                                  final TextButton menuButton,
                                  final TextButton color1Button,
                                  final TextButton color2Button,
                                  final TextButton color3Button,
                                  final TextButton color4Button) {

        cbSettingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(this.getClass().getSimpleName(), "colors updated");
                pref.setCbMode(!pref.getCbMode());
                System.out.println("Cb mode: " + pref.getCbMode());
                if (pref.getCbMode()){
                    pref.setColors("213,0,0", "240,228,66", "0,114,178", "86,180,233");
                    cbSettingsButton.setText("Colorblind mode [enabled]");
                } else {
                    pref.setColors("200,0,0", "0,200,0", "0,0,200", "200,200,0");
                    cbSettingsButton.setText("Colorblind mode [disabled]");
                }
            }
        });

        musicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(pref.isMusicEnabled()) {
                    assetManager.manager.get(Assets.MUSIC).pause();
                    pref.setMusic(false);
                    musicButton.setText("Music [disabled]");
                } else {
                    assetManager.manager.get(Assets.MUSIC).play();
                    pref.setMusic(true);
                    musicButton.setText("Music [enabled]");
                }
            }
        });

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.screenManager.setMenuScreen();
            }
        });

        color1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pref.setMainColor("color 1");
            }
        });

        color2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pref.setMainColor("color 2");
            }
        });

        color3Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pref.setMainColor("color 3");
            }
        });

        color4Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pref.setMainColor("color 4");
            }
        });
    }


}
