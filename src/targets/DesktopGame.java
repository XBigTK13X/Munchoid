package targets;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.Game;
import game.dev.DevConfig;
import sps.core.Logger;
import sps.core.SpsConfig;

import java.util.UUID;

public class DesktopGame {
    private static LwjglApplication instance;

    public static void main(String[] args) {
        if (args.length > 0) {
            for (String s : args) {
                if (s.equalsIgnoreCase("--play-as-bot")) {
                    DevConfig.BotEnabled = true;
                }
            }
        }
        if (DevConfig.BotEnabled) {
            Logger.setLogFile("bot-" + UUID.randomUUID().toString() + ".log");
        }
        else {
            Logger.setLogFile("game.log");
        }

        Logger.info("Launching the main game loop");
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Munchoid";
        if (SpsConfig.get().fullScreen) {
            cfg.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
            cfg.fullscreen = SpsConfig.get().fullScreen;
        }
        else {
            cfg.width = SpsConfig.get().resolutionWidth;
            cfg.height = SpsConfig.get().resolutionHeight;
        }
        cfg.vSyncEnabled = SpsConfig.get().vSyncEnabled;
        cfg.useGL20 = true;
        instance = new LwjglApplication(new Game(), cfg);
    }

    public static LwjglApplication get() {
        return instance;
    }
}
