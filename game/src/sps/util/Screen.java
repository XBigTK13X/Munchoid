package sps.util;

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

    private Screen() {

    }
}
