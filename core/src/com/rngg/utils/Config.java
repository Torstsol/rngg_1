package com.rngg.utils;

public class Config {
    private static class LazyHolder {
        static final Config INSTANCE = new Config();
    }

    public static String colorScheme;
    public static int soundFXVolume;
    public static int musicVolume;
    public static boolean muted;

    private Config() {
        colorScheme = "color.png"; // TODO Add default color scheme
        soundFXVolume = 10;
        musicVolume = 10;
        muted = false;
    }

    public static Config getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static void setColorScheme(String colorScheme) {
        // TODO Add a check that ensures that the color scheme exists
        Config.colorScheme = colorScheme;
    }

    public static void setSoundFXVolume(int volume) {
        if (volume >= 0 && volume <= 10) Config.soundFXVolume = volume;
    }

    public static void setMusicVolume(int volume) {
        if (volume >= 0 && volume <= 10) Config.musicVolume = volume;
    }

    public static void setMuted(boolean muted) {
        Config.muted = muted;
    }
}
