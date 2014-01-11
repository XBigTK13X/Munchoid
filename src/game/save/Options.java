package game.save;

import game.core.GameConfig;
import game.core.UserFiles;
import org.apache.commons.io.FileUtils;
import sps.core.Loader;
import sps.core.Logger;
import sps.core.SpsConfig;
import sps.display.Window;
import sps.io.InputBindings;
import sps.util.Maths;
import sps.util.Parse;

import java.io.File;

public class Options {
    private static final File __config = UserFiles.config();
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
                    case "soundEnabled":
                        options.SoundEnabled = Parse.bool(value);
                        break;
                    case "graphicsLowQuality":
                        options.GraphicsLowQuality = Parse.bool(value);
                        break;
                    case "tutorialEnabled":
                        options.TutorialEnabled = Parse.bool(value);
                        break;
                    case "tutorialQueryEnabled":
                        options.TutorialQueryEnabled = Parse.bool(value);
                        break;
                    case "showIntro":
                        options.ShowIntro = Parse.bool(value);
                        break;
                    case "brightness":
                        options.Brightness = Parse.inte(value);
                        break;
                    case "settingsDetected":
                        options.SettingsDetected = Parse.bool(value);
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
        if (UserFiles.input().exists()) {
            try {
                FileUtils.forceDelete(UserFiles.input());
            }
            catch (Exception e) {
                Logger.exception(e, false);
            }
            InputBindings.init();
        }
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
    public boolean FullScreen = false;
    public boolean MusicEnabled = true;
    public boolean SoundEnabled = true;
    public boolean GraphicsLowQuality;
    public boolean TutorialEnabled = true;
    public boolean ShowIntro = true;
    public boolean TutorialQueryEnabled = true;
    public int Brightness = 85;
    public boolean SettingsDetected = false;

    public void save() {
        try {
            String options = "";

            options += "windowedResolution=" + WindowResolutionX + "x" + WindowResolutionY + "\n";
            options += "fullScreen=" + FullScreen + "\n";
            options += "musicEnabled=" + MusicEnabled + "\n";
            options += "soundEnabled=" + SoundEnabled + "\n";
            options += "graphicsLowQuality=" + GraphicsLowQuality + "\n";
            options += "tutorialEnabled=" + TutorialEnabled + "\n";
            options += "tutorialQueryEnabled=" + TutorialQueryEnabled + "\n";
            options += "brightness=" + Brightness + "\n";
            options += "showIntro=" + ShowIntro + "\n";
            options += "settingsDetected=" + SettingsDetected;

            FileUtils.write(__config, options);
        }
        catch (Exception e) {
            Logger.exception(e, false);
        }
    }

    public void apply() {
        Window.resize(WindowResolutionX, WindowResolutionY, FullScreen);
        SpsConfig.get().musicEnabled = MusicEnabled;
        SpsConfig.get().soundEnabled = SoundEnabled;
        GameConfig.setGraphicsMode(GraphicsLowQuality);
        int brightness = (int) (Maths.percentToValue(-25, 0, Brightness));
        Window.get().screenEngine().setBrightness(brightness);
        Window.get(true).screenEngine().setBrightness(brightness);
    }
}
