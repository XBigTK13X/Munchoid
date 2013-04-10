package targets;

import game.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import sps.bridge.Bridge;
import sps.core.Logger;
import sps.core.SpsConfig;

public class DesktopGame {
    private static LwjglApplication instance;

    public static void main(String[] args) {

            Logger.setLogFile("game.log");
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
