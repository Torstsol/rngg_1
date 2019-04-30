package com.rngg.models;

import com.rngg.utils.RNG;

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
        return "HexMeshZone{" +
                "player=" + player +
                ", subZones=" + subZones +
                '}';
    }

    @Override
    public Iterator<HexZone> iterator() {
        return subZones.iterator();
    }

    public List<HexZone> getSubZones() {
        return subZones;
    }

    public HexZone getRandomSubZone() {
        if (subZones.size() == 0) return null;
        return subZones.get(RNG.nextInt(0, subZones.size() - 1));
    }

    public HexMeshZone getSuperZone() {
        return this;
    }
}
