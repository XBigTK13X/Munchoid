package sps.display;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL10;
import sps.color.Color;
import sps.core.Logger;
import sps.core.SpsConfig;
import sps.display.render.RenderStrategy;
import sps.display.render.Renderer;
import sps.display.render.StretchStrategy;

public class Window {
    private static RenderStrategy __defaultStrategy = new StretchStrategy();
    private static Renderer __dynamic;
    private static Renderer __fixed;
    private static boolean __tipHasBeenDisplayed = false;
    private static ApplicationListener __app;
    private static Color __bgColor = new Color(Color.BLACK);

    public static int Height;
    public static int Width;

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
            __dynamic.screenEngine().setStrategy(__defaultStrategy);
            __fixed = new Renderer(width, height);
            __fixed.screenEngine().setStrategy(__defaultStrategy);
            Logger.info("Window resolution: " + Gdx.graphics.getWidth() + "W, " + Gdx.graphics.getHeight() + "H");
            Width = width;
            Height = height;
        }
        return fixed ? __fixed : __dynamic;
    }

    public static void setRefreshInstance(ApplicationListener app) {
        __app = app;
    }

    public static void setWindowBackground(Color color) {
        __bgColor = color;
    }

    public static void resize(int width, int height, boolean changeWindow) {
        if (__app == null) {
            if (!__tipHasBeenDisplayed) {
                Logger.info("If the app is registered with Renderer.get().setRefreshInstance(this); in the create method, then the screen will update without a manual resizing.");
                __tipHasBeenDisplayed = true;
            }
        }
        else {
            if (changeWindow) {
                __app.resize(width, height);
                Gdx.graphics.setDisplayMode(width, height, Gdx.graphics.isFullscreen());
            }
        }
        Height = height;
        Width = width;
        get(true).screenEngine().resize(width, height);
        get(false).screenEngine().resize(width, height);
    }

    public static void setFullScreen(boolean enableFullScreen, int width, int height) {
        int resolutionX = 0;
        int resolutionY = 0;
        boolean isFullScreen = Gdx.graphics.isFullscreen();

        boolean apply = false;

        if (isFullScreen && !enableFullScreen) {
            resolutionX = width;
            resolutionY = height;
            apply = true;
        }
        else if (!isFullScreen && enableFullScreen) {
            Graphics.DisplayMode gfxNative = Gdx.graphics.getDesktopDisplayMode();
            resolutionX = gfxNative.width;
            resolutionY = gfxNative.height;
            apply = true;
        }

        if (apply) {
            Gdx.graphics.setDisplayMode(resolutionX, resolutionY, enableFullScreen);
            resize(resolutionX, resolutionY, false);
        }
    }

    public static void processDrawCalls() {
        Window.clear();
        get().processScheduledApiCalls();
        get(true).processScheduledApiCalls();
    }
}