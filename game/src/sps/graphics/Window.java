package sps.graphics;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import sps.core.Logger;
import sps.core.SpsConfig;

public class Window {
    private static RenderStrategy __defaultStrategy = new StretchStrategy();
    private static Renderer __dynamic;
    private static Renderer __fixed;
    private static Color __bgColor = Color.BLACK;

    private Window() {

    }

    public static void clear() {
        Gdx.gl.glClearColor(Window.__bgColor.r, Window.__bgColor.g, Window.__bgColor.b, Window.__bgColor.a);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public static Renderer get() {
        return get(false);
    }

    public static Renderer get(boolean fixed) {
        if (__dynamic == null || __fixed == null) {
            int width = SpsConfig.get().virtualWidth;
            int height = SpsConfig.get().virtualHeight;
            Logger.info("Virtual resolution: " + width + "W, " + height + "H");
            __dynamic = new Renderer(width, height);
            __dynamic.setStrategy(__defaultStrategy);
            __fixed = new Renderer(width, height);
            __fixed.setStrategy(__defaultStrategy);
            __fixed.setListening(true);
        }
        return fixed ? __fixed : __dynamic;
    }

    public static void setAllRefreshInstance(ApplicationListener app) {
        get(true).setRefreshInstance(app);
        get(false).setRefreshInstance(app);
    }

    public static void setAllStrategy(RenderStrategy strategy) {
        get(true).setStrategy(strategy);
        get(false).setStrategy(strategy);
    }

    public static void setWindowBackground(Color color) {
        __bgColor = color;
    }

    public static void resizeAll(int width, int height) {
        get(true).resize(width, height);
        get(false).resize(width, height);
    }
}