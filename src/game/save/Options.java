package game.save;

import com.badlogic.gdx.Gdx;
import game.GameConfig;
import org.apache.commons.io.FileUtils;
import sps.core.Loader;
import sps.core.Logger;
import sps.core.SpsConfig;
import sps.display.Window;
import sps.util.Parse;

import java.io.File;

public class Options {
    private static final File __config = Loader.get().userSave("Munchoid", "munchoid.cfg");
    private static final File __defaultConfig = Loader.get().data("default.munchoid.cfg");

    public static Options load() {
        Options options = new Options();
        try {
            if (!__config.exists()) {
                FileUtils.copyFile(__defaultConfig, __config);
            }
            for (String line : FileUtils.readLines(__config)) {
                String[] configs = line.split("=");
                String key = configs[0];
                String value = configs[1];

                switch (key) {
                    case "windowedResolution":
                        options.WindowResolutionX = Parse.inte(value.split("x")[0]);
                        options.WindowResolutionY = Parse.inte(value.split("x")[1]);
                        break;
                    case "fullScreen":
                        options.FullScreen = Parse.bool(value);
                        break;
                    case "musicEnabled":
                        options.MusicEnabled = Parse.bool(value);
                        break;
                    case "graphicsLowQuality":
                        options.GraphicsLowQuality = Parse.bool(value);
                        break;
                    case "tutorialEnabled":
                        options.TutorialEnabled = Parse.bool(value);
                        break;
                    case "enableTutorialQuery":
                        options.EnableTutorialQuery = Parse.bool(value);
                        break;
                    case "showIntro":
                        options.ShowIntro = Parse.bool(value);
                        break;
                    case "brightness":
                        options.Brightness = Parse.inte(value);
                        break;
                    default:
                        Logger.error("Invalid user config: " + line);
                        break;
                }
            }
        }
        catch (Exception e) {
            Logger.exception(e, false);
        }
        return options;
    }

    public static void resetToDefaults() {
        try {
            FileUtils.copyFile(__defaultConfig, __config);
            Options.load().apply();
        }
        catch (Exception e) {
            Logger.exception(e, false);
        }
    }

    private Options() {

    }

    public int WindowResolutionX;
    public int WindowResolutionY;
    public boolean FullScreen;
    public boolean MusicEnabled = true;
    public boolean GraphicsLowQuality;
    public boolean TutorialEnabled = true;
    public boolean ShowIntro = true;
    public boolean EnableTutorialQuery = true;
    public int Brightness = 0;

    public void save() {
        try {
            String options = "";

            options += "windowedResolution=" + WindowResolutionX + "x" + WindowResolutionY + "\n";
            options += "fullScreen=" + FullScreen + "\n";
            options += "musicEnabled=" + MusicEnabled + "\n";
            options += "graphicsLowQuality=" + GraphicsLowQuality + "\n";
            options += "tutorialEnabled=" + TutorialEnabled + "\n";
            options += "enableTutorialQuery=" + EnableTutorialQuery + "\n";
            options += "brightness=" + Brightness + "\n";
            options += "showIntro=" + ShowIntro;


            FileUtils.write(__config, options);
        }
        catch (Exception e) {
            Logger.exception(e, false);
        }
    }

    public void apply() {
        Window.setFullScreen(FullScreen, WindowResolutionX, WindowResolutionY);
        if (!Gdx.graphics.isFullscreen()) {
            Window.resize(WindowResolutionX, WindowResolutionY, true);
        }
        SpsConfig.get().musicEnabled = MusicEnabled;
        GameConfig.setGraphicsMode(GraphicsLowQuality);
        Window.get().screenEngine().setBrightness(Brightness);
        Window.get(true).screenEngine().setBrightness(Brightness);
    }
}
