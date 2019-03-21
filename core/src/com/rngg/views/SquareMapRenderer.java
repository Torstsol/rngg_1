package com.rngg.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rngg.models.SquareMap;
import com.rngg.models.SquareZone;

public class SquareMapRenderer extends MapRenderer<SquareMap, SquareZone> {

    public SquareMapRenderer(SquareMap map, ShapeRenderer sr, SpriteBatch batch, BitmapFont font) {
        super(map, sr, batch, font);
        this.shapeType = ShapeRenderer.ShapeType.Filled;
    }

    @Override
    public void drawZone(SquareZone zone) {
        sr.setColor((zone.getClicked()) ? Color.BLACK : zone.getPlayer().getColor());
        sr.rect(zone.getCol() * map.getZoneWidth(), zone.getRow() * map.getZoneHeight(),
                map.getZoneWidth(), map.getZoneHeight()
        );
    }

    @Override
    public void drawZoneText(SquareZone zone) {
        font.draw(batch, Integer.toString(zone.getUnits()),
                (zone.getCol() + 0.25f) * map.getZoneWidth(),
                (zone.getRow() + 0.50f) * map.getZoneHeight()
        );
    }
}