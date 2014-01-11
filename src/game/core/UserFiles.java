package game.core;

import sps.core.Loader;

import java.io.File;

public class UserFiles {
    private static final String _gameId = "Munchoid";

    public static File save() {
        return Loader.get().userSave(_gameId, "autosave.dat");
    }

    public static File config() {
        return Loader.get().userSave(_gameId, "munchoid.cfg");
    }

    public static File input() {
        return Loader.get().userSave(_gameId, "input.cfg");
    }
}
