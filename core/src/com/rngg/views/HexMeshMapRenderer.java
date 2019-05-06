package com.rngg.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rngg.models.HexMeshMap;
import com.rngg.models.HexMeshZone;
import com.rngg.models.HexZone;

import java.util.List;

public class HexMeshMapRenderer extends MapRenderer<HexMeshMap, HexMeshZone, List<List<List<int[]>>>> {

    public HexMeshMapRenderer(HexMeshMap map, ShapeRenderer sr, SpriteBatch batch, BitmapFont font) {
        super(map, sr, batch, font);

        this.shapeType = ShapeRenderer.ShapeType.Filled;
    }

    // Needed to make due for the offset in axial coordinates
    private int getCol(HexZone zone) {
        return zone.getCol() + (zone.getRow() / 2);
    }

    @Override
    public void drawZone(HexMeshZone superZone) {
        if (superZone == null) return;

        for (HexZone subZone : superZone) {
            float[] xPoints = new float[7];
            float[] yPoints = new float[7];

            // Center points for the zone. Offset to not render from coordinate (0, 0)
            float centerX = map.getSize() / 2
                    + map.getSize() * (float) (Math.sqrt(3) * getCol(subZone)
                    + Math.sqrt(3) / 2) - (float) (Math.sqrt(3) * map.getSize() / 2);

            float centerY = map.getSize() + map.getSize() * (3f / 2 * subZone.getRow());

            // Offsets every other row, so they are not generated in a straight line
            if (subZone.getRow() % 2 != 0) {
                centerX += map.getZoneWidth() / 2;
            }

            float angleDeg;
            float angleRad;

            /*
             *   Renders all points that makes up the hex.
             *   The hex is rendered as a collection of triangles, hence the vast point generation
             */
            for (int i = 0; i < 6; i++) {
                angleDeg = (float) (60 * i - 30);
                angleRad = (float) (Math.PI / 180 * angleDeg);

                xPoints[i] = (float) (centerX + map.getSize() * Math.cos(angleRad));
                yPoints[i] = (float) (centerY + map.getSize() * Math.sin(angleRad));
            }

            xPoints[6] = xPoints[0];
            yPoints[6] = yPoints[0];

            for (int i = 0; i < 6; i++) {
                sr.setColor((superZone.getClicked()) ? Color.GRAY : superZone.getPlayer().getColor());
                sr.triangle(centerX, centerY,
                        xPoints[i], yPoints[i],
                        xPoints[i + 1], yPoints[i + 1]);
            }

            for (HexZone neighbor : subZone.getNeighbors()) {
                if (neighbor.getSuperZone() != subZone.getSuperZone()) {
                    int neighborRow = neighbor.getRow();
                    int neighborCol = neighbor.getCol();
                    int subZoneRow = subZone.getRow();
                    int subZoneCol = subZone.getCol();

                    float size = map.getSize();
                    float width = (float) Math.sqrt(3) * size;
                    float height = 2 * size;

                    float x1 = -1;
                    float x2 = -1;
                    float y1 = -1;
                    float y2 = -1;

                    if (neighborRow == subZoneRow) {
                        y1 = centerY + size / 2;
                        y2 = centerY - size / 2;

                        x1 = (neighborCol == subZoneCol - 1) ? centerX - (width / 2) : centerX + (width / 2);
                        x2 = x1;
                    } else if (neighborRow == subZoneRow - 1) {
                        y1 = centerY - (size / 2);
                        y2 = centerY - size;

                        x1 = (neighborCol == subZoneCol + 1) ? centerX + (width / 2) : centerX - (width / 2);
                        x2 = centerX;
                    } else if (neighborRow == subZoneRow  + 1) {
                        y1 = centerY + (size / 2);
                        y2 = centerY + size;

                        x1 = (neighborCol == subZoneCol) ? centerX + (width / 2) : centerX - (width / 2);
                        x2 = centerX;
                    }

                    sr.rectLine(x1, y1, x2, y2, 3f, Color.BLACK, Color.BLACK);
                }
            }
        }
    }

    @Override
    public void drawZoneText(HexMeshZone zone) {
        if (zone == null || zone.getSubZones().size() == 0) return;

        List<HexZone> subZones = zone.getSubZones();
        HexZone randomZone = subZones.get(subZones.size() / 2);

        // Center points for the zone. Offset to not render from coordinate (0, 0)
        float centerX = map.getSize() / 2
                + map.getSize() * (float) (Math.sqrt(3) * getCol(randomZone)
                + Math.sqrt(3) / 2) - (float) (Math.sqrt(3) * map.getSize() / 2);

        float centerY = map.getSize() + map.getSize() * (3f / 2 * randomZone.getRow());

        // Offsets every other row, so they are not generated in a straight line
        if (randomZone.getRow() % 2 != 0) {
            centerX += map.getZoneWidth() / 2;
        }

        float fontScaleX = font.getScaleX();
        float fontScaleY = font.getScaleY();

        font.getData().setScale(map.getZoneWidth() / 1f / map.getSize() / 2);
        font.draw(batch, Integer.toString(zone.getUnits()),
                centerX,
                centerY
        );

        font.getData().setScale(fontScaleX, fontScaleY);
    }
}
