package game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.color.Color;
import sps.color.Colors;
import sps.core.Point2;
import sps.core.RNG;
import sps.display.Screen;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.draw.TextureManipulation;
import sps.entities.HitTest;

import java.util.ArrayList;
import java.util.List;

public class BackgroundMaker {
    private BackgroundMaker() {

    }

    private static enum ModelId {
        Empty,
        Via,
        Trace
    }

    public static Sprite noisyRadialDark() {
        Color[][] base = radialDarkBase((int) Screen.width(100), (int) Screen.height(100));
        TextureManipulation.subtleNoise(base, 5);
        return SpriteMaker.get().fromColors(base);
    }

    public static Sprite noisyRadialBright() {
        Color[][] base = radialBrightBase((int) Screen.width(100), (int) Screen.height(100));
        TextureManipulation.subtleNoise(base, 40);
        return SpriteMaker.get().fromColors(base);
    }

    public static Sprite radialBright() {
        return radialBright((int) Screen.width(100), (int) Screen.height(100));
    }

    public static Sprite radialBright(int pixelWidth, int pixelHeight) {

        return SpriteMaker.get().fromColors(radialBrightBase(pixelWidth, pixelHeight));
    }

    public static Sprite radialDark() {
        return radialDark((int) Screen.width(100), (int) Screen.height(100));
    }


    public static Sprite radialDark(int pixelWidth, int pixelHeight) {
        return SpriteMaker.get().fromColors(radialDarkBase(pixelWidth, pixelHeight));
    }

    private static Color[][] radialBrightBase(int w, int h) {
        Color c1 = Colors.randomPleasant();
        Color c2 = new Color(1f, 1f, 1f, 1f);
        return ProcTextures.smoothRadial(w, h, c1, c2);
    }

    private static Color[][] radialDarkBase(int w, int h) {
        Color c1 = Colors.randomPleasant();
        Color c2 = Colors.brightnessShift(c1, -85);
        return ProcTextures.smoothRadial(w, h, c1, c2);
    }

    public static Sprite printedCircuitBoard() {
        return printedCircuitBoard((int) Screen.width(100), (int) Screen.height(100));
    }

    public static Sprite printedCircuitBoard(int pixelWidth, int pixelHeight) {
        return convertModelToTexture(buildModel(pixelWidth, pixelHeight));
    }

    private static final int viaPixelWidth = 12;
    private static final int viaPixelMargin = 10;
    private static final int tracePixelWidth = 3;

    private static ModelId[][] buildModel(int width, int height) {
        List<Point2> vias = new ArrayList<Point2>();

        ModelId[][] result = new ModelId[width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                result[ii][jj] = ModelId.Empty;
            }
        }

        //Via placement
        int screenArea = width * height;
        int viaCount = screenArea / (viaPixelWidth * viaPixelWidth) / (viaPixelMargin * viaPixelMargin);
        while (viaCount > 0) {
            viaCount--;
            Point2 via = new Point2(RNG.next(width), RNG.next(height));
            result[(int) via.X][(int) via.Y] = ModelId.Via;
            vias.add(via);
        }

        //Trace connections
        while (vias.size() > 0) {
            Point2 start = RNG.pick(vias);
            Point2 end = RNG.pick(vias);
            if (start != end) {
                Point2 maxx = (start.X > end.X) ? start : end;
                Point2 minx = (start.X > end.X) ? end : start;
                Point2 maxy = (start.Y > end.Y) ? start : end;
                Point2 miny = (start.Y > end.Y) ? end : start;

                int xtrace = (int) minx.X;
                while (xtrace < maxx.X) {
                    xtrace++;
                    for (int w = -tracePixelWidth / 2; w < tracePixelWidth / 2; w++) {
                        int y = (int) minx.Y + w;
                        if (y >= 0 && y < result[0].length) {
                            if (result[xtrace][y] == ModelId.Empty) {
                                result[xtrace][y] = ModelId.Trace;
                            }
                        }
                    }
                }

                int ytrace = (int) miny.Y;
                while (ytrace < maxy.Y) {
                    ytrace++;
                    for (int w = -tracePixelWidth / 2; w < tracePixelWidth / 2; w++) {
                        int x = (int) maxx.X + w;
                        if (x >= 0 && x < result.length) {
                            if (result[x][ytrace] == ModelId.Empty) {
                                result[x][ytrace] = ModelId.Trace;
                            }
                        }
                    }
                }
            }
            vias.remove(end);
            vias.remove(start);
        }
        return result;
    }

    private static Sprite convertModelToTexture(ModelId[][] model) {
        Color board = Colors.randomPleasant();
        Color board2 = Colors.randomPleasant();
        int w = model.length;
        int h = model[0].length;
        Color[][] base = ProcTextures.smoothRadial(w, h, board, board2);

        //Create the via texture
        boolean[][] viaBase = new boolean[viaPixelWidth][viaPixelWidth];
        Point2 viaCenter = new Point2(viaPixelWidth / 2, viaPixelWidth / 2);
        for (int ii = 0; ii < viaBase.length; ii++) {
            for (int jj = 0; jj < viaBase[0].length; jj++) {
                float dist = HitTest.getDistance(ii, jj, viaCenter.X, viaCenter.Y);
                if (dist < viaPixelWidth / 2) {
                    viaBase[ii][jj] = true;
                }
            }
        }
        Color trace = Colors.randomPleasant();
        trace = Colors.brightnessShift(trace, -5);

        Color via = Colors.randomPleasant();
        via = Colors.brightnessShift(via, -5);
        Color via2 = Colors.compliment(via);

        //Draw the traces first, then the vias
        for (int pass = 1; pass < 3; pass++) {
            for (int ii = 0; ii < model.length; ii++) {
                for (int jj = 0; jj < model[ii].length; jj++) {
                    if (pass == 1 && model[ii][jj] == ModelId.Trace) {
                        base[ii][jj] = trace;
                    }
                    if (pass == 2 && model[ii][jj] == ModelId.Via) {
                        Color v = RNG.coinFlip() ? via : via2;
                        for (int ox = 0; ox < viaBase.length; ox++) {
                            for (int oy = 0; oy < viaBase[0].length; oy++) {
                                if (viaBase[ox][oy]) {
                                    int x = ii + ox - viaPixelWidth / 2;
                                    int y = jj + oy - viaPixelWidth / 2;
                                    if (x >= 0 && y >= 0 && x < base.length && y < base[0].length) {
                                        base[x][y] = v;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        base = TextureManipulation.blurStack(base, 3);

        TextureManipulation.darken(base, 60);


        return SpriteMaker.get().fromColors(base);
    }
}
