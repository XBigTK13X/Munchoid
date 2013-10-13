package targets;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import sps.bridge.SpriteTypes;
import sps.bridge.Sps;
import sps.core.RNG;
import sps.core.SpsConfig;
import sps.display.render.FrameStrategy;
import sps.display.SpriteSheetManager;
import sps.display.Window;
import sps.io.DefaultStateProvider;
import sps.io.Input;

public class DummyApp {
    private ApplicationListener _app;

    public DummyApp(ApplicationListener sideloadedApp) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.useGL20 = true;
        cfg.width = SpsConfig.get().resolutionWidth;
        cfg.height = SpsConfig.get().resolutionHeight;
        new LwjglApplication(sideloadedApp, cfg);
        _app = sideloadedApp;
    }

    public void create() {
        RNG.seed((int) System.currentTimeMillis());
        Sps.setup(true);
        Window.setWindowBackground(Color.BLACK);
        Window.get(false).screenEngine().setStrategy(new FrameStrategy());
        Window.get(true).screenEngine().setStrategy(new FrameStrategy());
        Window.setRefreshInstance(_app);
        Input.get().setup(new DefaultStateProvider());
        SpriteSheetManager.setup(SpriteTypes.getDefs());
    }
}
