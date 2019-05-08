package com.rngg.views;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rngg.game.Rngg;
import com.rngg.models.GameMap;
import com.rngg.models.Zone;
import com.rngg.utils.Assets;
import com.rngg.utils.GameAssetManager;

import java.util.List;

public abstract class MapRenderer<M extends GameMap<Z, ZoneFormat>, Z extends Zone, ZoneFormat extends List> {
    protected M map;
    protected ShapeRenderer sr;
    protected SpriteBatch batch;
    protected BitmapFont font;
    protected ShapeRenderer.ShapeType shapeType;

    public MapRenderer(M map,ShapeRenderer sr, SpriteBatch batch, BitmapFont font) {
        this.map = map;
        this.sr = sr;
        this.batch = batch;
        this.font = font;
    }

    public void draw() {
        /* batch.begin();
        batch.draw(GameAssetManager.getManager().get(Assets.BACKGROUND_GAME), 0, 0, Rngg.WIDTH, Rngg.HEIGHT);
        batch.end(); */

        sr.begin(shapeType);
        for (Z zone : map._getZones()) {
            drawZone(zone);
        }
        sr.end();

        batch.begin();
        for (Z zone : map._getZones()) {
            drawZoneText(zone);
        }
        batch.end();
    }

    public abstract void drawZone(Z zone);

    public abstract void drawZoneText(Z zone);
}
