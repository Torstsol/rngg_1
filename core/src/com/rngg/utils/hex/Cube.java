package com.rngg.utils.hex;

public class Cube {
    private double x;
    private double y;
    private double z;

    public Cube(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Hex cubeToAxial(Cube cube) {
        return new Hex(cube.x, cube.z);
    }

    public static Cube cubeRound(Cube cube) {
        double rx = Math.round(cube.x);
        double ry = Math.round(cube.y);
        double rz = Math.round(cube.z);

        double xDiff = Math.abs(rx - cube.x);
        double yDiff = Math.abs(ry - cube.y);
        double zDiff = Math.abs(rz - cube.z);

        if (xDiff > yDiff && xDiff > zDiff) {
            rx = -ry - rz;
        } else if (yDiff > zDiff) {
            ry = -rx - rz;
        } else {
            rz = -rx - ry;
        }

        return new Cube(rx, ry, rz);
    }
}
