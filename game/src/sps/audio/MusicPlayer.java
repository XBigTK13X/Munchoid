package sps.audio;

import sps.core.SpsConfig;

public abstract class MusicPlayer {
    private static MusicPlayer __instance;
    private static MusicPlayer togglePlayer;

    public static void set(MusicPlayer musicPlayer) {
        reset();
        __instance = musicPlayer;
    }

    public static void reset() {
        if (__instance != null) {
            __instance.stop();
        }
        __instance = null;
    }

    public static MusicPlayer get(MusicPlayer player) {

        if (__instance == null) {
            if (SpsConfig.get().musicEnabled && player != null) {
                __instance = player;
            }
            else {
                __instance = new MuteMusicPlayer();
            }
        }
        else {
            if (SpsConfig.get().musicEnabled) {
                __instance.stop();
                __instance = player;
            }
        }
        return __instance;
    }

    public static MusicPlayer get() {
        if (__instance == null) {
            __instance = new MuteMusicPlayer();
        }
        return __instance;
    }

    public static void toggle() {
        __instance.stop();
        if (togglePlayer == null) {
            togglePlayer = __instance;
            __instance = new MuteMusicPlayer();
        }
        else {
            __instance = togglePlayer;
            togglePlayer = null;
        }
        __instance.start();
    }

    public abstract void start();

    public abstract void stop();
}
