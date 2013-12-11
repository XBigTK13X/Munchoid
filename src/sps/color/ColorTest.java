package sps.color;

import com.badlogic.gdx.graphics.Color;
import sps.core.Logger;

public class ColorTest {
    //Expectations taken from: http://www.workwithcolor.com/color-converter-01.htm
    public static void main(String[] args) {

        Logger.info("Null test: BLACK");
        test(Color.BLACK, 0, 0, 0, 0, 0, 0);

        Logger.info("Null test: WHITE");
        test(Color.WHITE, 0, 0, 1, 100, 0, 0);

        Logger.info("Gauge test: Dull purple/pink");
        test(new RGBA(145, 125, 130).toColor(), 3.45f, .14f, .57f, 54, 9, 0);

    }

    private static void test(Color color, float... ex) {
        Color base = new Color(color);
        HSV hsv = HSV.fromColor(base);
        CIELab lab = CIELab.fromRGB(base.r, base.g, base.b);


        Logger.info("Test 1:" + (ex[0] == hsv.H) + ", Expected: " + ex[0] + ", got: " + hsv.H);
        Logger.info("Test 2:" + (ex[1] == hsv.S) + ", Expected: " + ex[1] + ", got: " + hsv.S);
        Logger.info("Test 3:" + (ex[2] == hsv.V) + ", Expected: " + ex[2] + ", got: " + hsv.V);
        Logger.info("Test 4:" + (ex[3] == lab.L) + ", Expected: " + ex[3] + ", got: " + lab.L);
        Logger.info("Test 5:" + (ex[4] == lab.A) + ", Expected: " + ex[4] + ", got: " + lab.A);
        Logger.info("Test 6:" + (ex[5] == lab.B) + ", Expected: " + ex[5] + ", got: " + lab.B);

    }
}
