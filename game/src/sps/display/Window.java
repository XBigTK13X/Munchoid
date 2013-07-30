package sps.display;

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
    private static boolean __tipHasBeenDisplayed = false;
    private static ApplicationListener __app;
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

    public static void setRefreshInstance(ApplicationListener app) {
        __app = app;
    }

    public static void setWindowBackground(Color color) {
        __bgColor = color;
    }

    public static void resize(int width, int height) {
        if (__app != null) {
            //TODO See if this is still needed
            // __app.resize(Screen.get().VirtualWidth, Screen.get().VirtualHeight);
        }
        else {
            if (!__tipHasBeenDisplayed) {
                Logger.info("If the app is registered with Renderer.get().setRefreshInstance(this); in the create method, then the screen will update without a manual resizing.");
                __tipHasBeenDisplayed = true;
            }
        }
        get(true).resize(width, height);
        get(false).resize(width, height);
    }
}