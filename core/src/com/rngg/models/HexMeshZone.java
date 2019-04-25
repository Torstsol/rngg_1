package com.rngg.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HexMeshZone extends Zone implements Iterable<HexZone> {
    private List<HexZone> subZones;

    public HexMeshZone(Player player) {
        this.player = player;
        this.units = (int) ((Math.random() * 7) + 1);

        this.subZones = new ArrayList<HexZone>();
    }

    public void addSubZone(HexZone zone) {
        subZones.add(zone);
    }

    @Override
    public String toString() {
        return "HexZone{" +
                ", player=" + player +
                '}';
    }

    @Override
    public Iterator<HexZone> iterator() {
        return subZones.iterator();
    }
}
