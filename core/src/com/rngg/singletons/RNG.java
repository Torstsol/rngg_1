package com.rngg.singletons;

// TODO only used for Arrays.toString() in main, remove before merging
import java.util.Arrays;

public class RNG {
    private static RNG instance = null;
    private String[] labels;
    private float[] values, probabilities;

    // TODO main for testing purposes, remove before merging
    public static void main(String[] args) {
        RNG rng = RNG.d6();
        int[] roll = rng.roll(8);
        System.out.println(Arrays.toString(rng.labelFromRoll(roll)));
        System.out.println(rng.valueFromRoll(roll));
    }

    public RNG getInstance() {
        return instance;
    }

    private RNG(String[] labels, float[] values, float[] probabilities) {
        if (labels.length != values.length
                || labels.length != probabilities.length
                // my (@sondremb's) AndroidStudio throws a hissy fit at below line,
                // and says that the condition is always false.
                // Testing determined that to be a lie.
                || values.length != probabilities.length) {
            throw new IllegalArgumentException("Arguments must have same length");
        }

        float sum = 0;
        for (float p : probabilities) {
            sum += p;
        }
        if (!almostEqual(sum, 1)) {
            throw new IllegalArgumentException("Probability array must sum to 1 (was " + sum + ")");
        }

        this.labels = labels;
        this.values = values;
        this.probabilities = probabilities;
    }

    // --- BEGIN rolling logic ---

    public int roll() {
        // rolls the dice once, returns index rolled
        double roll = Math.random();
        double sum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            if (sum >= roll) {
                return i;
            }
        }
        // only gets here if sum of probabilities is slightly less than 1,
        // and Math.random() returns between that sum and 1.
        // This is fairly unlikely, but entirely possible
        return probabilities.length - 1;
    }

    public int[] roll(int n) {
        // roll multiple dice at once
        int[] ret = new int[n];
        for (int i = 0; i < n; i++) {
            ret[i] = roll();
        }
        return ret;
    }

    public float valueFromRoll(int i) {
        // returns value under index i (output from roll function)
        return values[i];
    }

    public float valueFromRoll(int[] is) {
        // takes an array of indices, returns sum of values
        float sum = 0;
        for (int i : is) {
            sum += values[i];
        }
        return sum;
    }

    public String labelFromRoll(int i) {
        // returns label under index i (output from roll function)
        return labels[i];
    }

    public String[] labelFromRoll(int[] is) {
        // takes an array of indices, returns array of labels
        String[] ret = new String[is.length];
        for (int j = 0; j < is.length; j++) {
            ret[j] = labels[is[j]];
        }
        return ret;
    }

    // --- END rolling logic ---

    // --- BEGIN useful instances ---

    public static RNG uniformRange(int min, int max, int step) {
        // creates an RNG with uniformly distributed integer values in a range
        // labels are values as strings

        int length = ((max - min) / step) + 1;

        String[] labels = new String[length];
        float[] values = new float[length];
        float[] probabilities = new float[length];

        int j = 0;
        for (int i = min; i <= max; i += step) {
            labels[j] = Integer.toString(i);
            values[j] = (float) i;
            probabilities[j] = (float) 1 / length;
            j += 1;
        }

        RNG.instance = new RNG(labels, values, probabilities);
        return RNG.instance;
    }

    public static RNG uniformRange(int min, int max) {
        // overloads uniformRange with implicit step=1
        return uniformRange(min, max, 1);
    }

    public static RNG d6() {
        return uniformRange(1, 6);
    }

    public static RNG d20() {
        return uniformRange(1, 20);
    }

    public static RNG grades() {
        RNG.instance = new RNG(
                new String[]{"F", "E", "D", "C", "B", "A"},
                new float[]{0, 41, 53, 65, 77, 89},
                new float[]{41/101f, 12/101f, 12/101f, 12/101f, 12/101f, 12/101f}
        );
        return RNG.instance;
    }

    // --- END useful instances ---

    private boolean almostEqual(float x, float y) {
        // floating point math is hard, and n * 1/n is not necessarily equal to 1
        // checks if the two inputs are "close enough"
        return Math.abs(x-y) <= 1e-5;
    }
}
