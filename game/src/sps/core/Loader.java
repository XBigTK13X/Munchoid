package sps.core;

import java.io.File;
import java.util.HashMap;

public class Loader {

    private static Loader instance;

    public static Loader get() {
        if (instance == null) {
            instance = new Loader();
        }
        return instance;
    }

    private final HashMap<String, File> files = new HashMap<String, File>();

    private final String root = "assets";

    private final String graphics = "graphics";
    private final String data = "data";
    private final String music = "music";
    private final String sound = "sound";
    private final String font = "font";

    private File get(String dir, String target) {
        return new File(root + "/" + dir + "/" + target);
    }

    public File data(String target) {
        return get(data, target);
    }

    public File graphics(String target) {
        return get(graphics, target);
    }

    public File music(String target) {
        return get(music, target);
    }

    public File sound(String target) {
        return get(sound, target);
    }

    public File font(String target) {
        return get(graphics + "/" + font, target);
    }

    public File save(String target) {
        return data("autoSave/" + target);
    }
}
