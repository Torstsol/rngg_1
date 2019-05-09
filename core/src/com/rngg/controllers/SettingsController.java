package com.rngg.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.rngg.configuration.GamePreferences;
import com.rngg.game.Rngg;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;
import com.rngg.utils.RNG;

public class SettingsController extends Controller {

    private GamePreferences pref;
    private AssetManager assetManager;

    public SettingsController(Rngg game) {
        super(game);

        pref = GamePreferences.getInstance();
        assetManager = GameAssetManager.getManager();
    }

    @Override
    public void update(float delta) {}

    public void addActorListeners(final TextButton cbSettingsButton,
                                  final TextButton musicButton,
                                  final TextButton menuButton,
                                  final TextButton color1Button,
                                  final TextButton color2Button,
                                  final TextButton color3Button,
                                  final TextButton color4Button,
                                  final TextButton mapButton,
                                  final TextButton diceButton,
                                  final Slider slider,
                                  final Label sliderLabel,
                                  final SelectBox<String> customMaps) {

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
                    assetManager.get(Assets.MUSIC).pause();
                    pref.setMusic(false);
                    musicButton.setText("Music [disabled]");
                } else {
                    assetManager.get(Assets.MUSIC).play();
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

        mapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String map = pref.getMapType();

                pref.setCustomMapEnabled(false);

                if (map.equals("SquareMap")){
                    pref.setMapType("HexMap");
                    mapButton.setText("Hexagonal map");
                } else if (map.equals("HexMap")) {
                    pref.setMapType("HexMeshMap");
                    mapButton.setText("HexMeshMap");
                } else if (map.equals("HexMeshMap")) {
                    pref.setMapType("CustomMap");
                    mapButton.setText("Custom Map");
                    pref.setCustomMapEnabled(true);
                } else {
                    pref.setMapType("SquareMap");
                    mapButton.setText("Square map");
                }
            }
        });

        diceButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (pref.getDiceType().equals(RNG.D6)){
                    pref.setDiceType(RNG.D20);
                    diceButton.setText(RNG.D20);
                } else {
                    pref.setDiceType(RNG.D6);
                    diceButton.setText(RNG.D6);
                }
            }
        });

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pref.setNumDice((int) slider.getValue());
                sliderLabel.setText("Number of dice: " + pref.getNumDice());
            }
        });

        customMaps.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pref.setCustomMap("levels/" + customMaps.getSelected() + ".json");
            }
        });
    }


}
