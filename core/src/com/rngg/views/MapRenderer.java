package com.rngg.views;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rngg.models.GameMap;
import com.rngg.models.Zone;

public abstract class MapRenderer<M extends GameMap<Z>, Z extends Zone> {
    protected M map;
    protected ShapeRenderer sr;
    protected SpriteBatch batch;
    protected BitmapFont font;
    protected ShapeRenderer.ShapeType shapeType;

    public MapRenderer(M map, BitmapFont font) {
        this.map = map;
        this.sr = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.font = font;
    }

    public void draw() {
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
