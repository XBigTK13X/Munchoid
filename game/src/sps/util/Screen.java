package sps.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import sps.core.Point2;
import sps.core.RNG;
import sps.graphics.Renderer;

public class Screen {
    public static float height(int percent) {
        return Renderer.get().VirtualHeight * ((float) percent / 100);
    }

    public static float width(int percent) {
        return Renderer.get().VirtualWidth * ((float) percent / 100);
    }

    public static Point2 pos(int widthPercent, int heightPercent) {
        return new Point2(width(widthPercent), height(heightPercent));
    }

    public static Point2 rand(int widthPercentMin, int widthPercentMax, int heightPercentMin, int heightPercentMax) {
        return new Point2(width(RNG.next(widthPercentMin, widthPercentMax)), height(RNG.next(heightPercentMin, heightPercentMax)));
    }

    private static Screen __instance;

    public static Screen get() {
        if (__instance == null) {
            __instance = new Screen();
        }
        return __instance;
    }

    private Pixmap textureBase;

    private Screen() {
        textureBase = new Pixmap(Renderer.get().VirtualWidth, Renderer.get().VirtualHeight, Pixmap.Format.RGBA8888);

    }

    private Texture t;

    public void draw() {
        if (t != null) {
            t.dispose();
        }
        t = new Texture(textureBase);
        Renderer.get().draw(t);
        textureBase.setColor(Renderer.get().getWindowBackground().toIntBits());
        textureBase.fill();
    }

    public void setPixel(float x, float y, Color color) {
        textureBase.setColor(color);
        textureBase.drawPixel((int) x, Renderer.get().VirtualHeight - (int) y);
    }
}
