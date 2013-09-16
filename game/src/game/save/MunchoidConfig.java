package game.save;

import game.GameConfig;
import org.apache.commons.io.FileUtils;
import sps.core.Loader;
import sps.core.Logger;
import sps.core.SpsConfig;
import sps.display.Window;
import sps.util.Parse;

import java.io.File;

public class MunchoidConfig {
    private static final File __config = Loader.get().save("munchoid.cfg");
    private static final File __defaultConfig = Loader.get().save("default.munchoid.cfg");

    private MunchoidConfig() {

    }

    public static void reload() {
        try {
            if (!__config.exists()) {
                FileUtils.copyFile(__defaultConfig, __config);
            }
            for (String line : FileUtils.readLines(__config)) {
                String[] configs = line.split("=");
                String key = configs[0];
                String value = configs[1];
                switch (key) {
                    case "resolution":
                        int resX = Parse.inte(value.split("x")[0]);
                        int resY = Parse.inte(value.split("x")[1]);
                        Window.resize(resX, resY, true);
                        break;
                    case "fullScreen":
                        boolean fullScreen = Parse.bool(value);
                        if (SpsConfig.get().fullScreen != fullScreen) {
                            Window.get().toggleFullScreen();
                        }
                        break;
                    case "musicEnabled":
                        boolean musicEnabled = Parse.bool(value);
                        SpsConfig.get().musicEnabled = musicEnabled;
                        break;
                    case "graphicsLowQuality":
                        GameConfig.setGraphicsMode(Parse.bool(value));
                        break;
                    default:
                        break;
                }
            }
        }
        catch (Exception e) {
            Logger.exception(e, false);
        }
    }
}
