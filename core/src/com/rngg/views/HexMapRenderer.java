package com.rngg.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rngg.models.HexMap;
import com.rngg.models.HexZone;

import java.util.List;

public class HexMapRenderer extends MapRenderer<HexMap, HexZone, List<List<int[]>>> {

    public HexMapRenderer(HexMap map, ShapeRenderer sr, SpriteBatch batch, BitmapFont font) {
        super(map, sr, batch, font);

        this.shapeType = ShapeRenderer.ShapeType.Filled;
    }

    private int getCol(HexZone zone) {
        return zone.getCol() + (zone.getRow() / 2);
    }

    @Override
    public void drawZone(HexZone zone) {
        if (zone == null) return;

        sr.setColor((zone.getClicked()) ? Color.BLACK : zone.getPlayer().getColor());

        float[] xPoints = new float[7];
        float[] yPoints = new float[7];
        float centerX = map.getSize() * (float) (Math.sqrt(3) * getCol(zone) + Math.sqrt(3) / 2) - (float) (Math.sqrt(3) * map.getSize() / 2);
        float centerY = map.getSize() * (3f / 2 * zone.getRow());

        if (zone.getRow() % 2 != 0) {
            centerX += map.getZoneWidth() / 2;
        }

        float angleDeg;
        float angleRad;
        for (int i = 0; i < 6; i++) {
            angleDeg = (float) (60 * i - 30);
            angleRad = (float) (Math.PI / 180 * angleDeg);

            xPoints[i] = (float) (centerX + map.getSize() * Math.cos(angleRad));
            yPoints[i] = (float) (centerY + map.getSize() * Math.sin(angleRad));
        }

        xPoints[6] = xPoints[0];
        yPoints[6] = yPoints[0];

        for (int i = 0; i < 6; i++) {
            sr.triangle(centerX, centerY,
                    xPoints[i], yPoints[i],
                    xPoints[i + 1], yPoints[i + 1]);
        }
    }

    @Override
    public void drawZoneText(HexZone zone) {
        if (zone == null) return;

        float centerX = map.getSize() * (float) (Math.sqrt(3) * getCol(zone) + Math.sqrt(3) / 2) - (float) (Math.sqrt(3) * map.getSize() / 2);
        float centerY = map.getSize() * (3f / 2 * zone.getRow());

        if (zone.getRow() % 2 != 0) {
            centerX += map.getZoneWidth() / 2;
        }

        font.draw(batch, Integer.toString(zone.getUnits()),
                centerX,
                centerY
        );
    }
}
