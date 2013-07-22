package targets;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.Game;
import game.GameConfig;
import sps.core.Logger;
import sps.core.SpsConfig;

import java.util.UUID;

public class DesktopGame {
    private static LwjglApplication instance;

    public static void main(String[] args) {
        if (GameConfig.DevBotEnabled) {
            Logger.setLogFile("logs/" + UUID.randomUUID().toString() + ".log");
        }
        else {
            Logger.setLogFile("game.log");
        }
        Logger.info("Launching the main game loop");
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Game";
        if (SpsConfig.get().fullScreen) {
            cfg.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
            cfg.fullscreen = SpsConfig.get().fullScreen;
        }
        else {
            cfg.width = SpsConfig.get().resolutionWidth;
            cfg.height = SpsConfig.get().resolutionHeight;
        }
        cfg.useGL20 = true;
        instance = new LwjglApplication(new Game(), cfg);
    }

    public static LwjglApplication get() {
        return instance;
    }
}
