package com.rngg.utils.hex;

public class Hex {
    double q;
    double r;

    public Hex(double q, double r) {
        this.q = q;
        this.r = r;
    }

    public static Cube axialToCube(Hex hex) {
        double x = hex.q;
        double z = hex.r;
        double y = -x - z;

        return new Cube(x, y, z);
    }

    public static Hex hexRound(Hex hex) {
        return Cube.cubeToAxial(Cube.cubeRound(Hex.axialToCube(hex)));
    }

    public double getQ() {
        return q;
    }

    public double getR() {
        return r;
    }
}
