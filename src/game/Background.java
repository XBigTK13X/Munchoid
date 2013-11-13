package game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.core.Point2;
import sps.core.RNG;
import sps.display.Screen;
import sps.draw.Colors;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;

import java.util.ArrayList;
import java.util.List;

public class Background {
    private Background() {

    }

    private static enum ModelId {
        Empty,
        Via,
        Trace
    }

    private static final int viaPixelWidth = 10;
    private static final int viaPixelMargin = 10;

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
                Point2 max = (start.X > end.X) ? start : end;
                Point2 min = (start.X > end.X) ? end : start;
                int xtrace = (int) min.X;
                while (xtrace < max.X) {
                    xtrace++;
                    int y = (int) min.Y;
                    if (result[xtrace][y] == ModelId.Empty) {
                        result[xtrace][y] = ModelId.Trace;
                    }
                }

                max = (start.Y > end.Y) ? start : end;
                min = (start.Y > end.Y) ? end : start;
                int ytrace = (int) min.Y;
                while (ytrace < max.Y) {
                    ytrace++;
                    int x = (int) min.X;
                    if (result[x][ytrace] == ModelId.Empty) {
                        result[x][ytrace] = ModelId.Trace;
                    }
                }
            }
            vias.remove(end);
            vias.remove(start);
        }
        return result;
    }

    private static Sprite convertModelToTexture(ModelId[][] model) {
        Color via = Colors.randomPleasant();
        Color trace = Colors.randomPleasant();
        Color board = Colors.shade(Colors.randomPleasant(), -60);

        Color[][] base = ProcTextures.monotone((int) Screen.width(100), (int) Screen.height(100), board);


        for (int ii = 0; ii < model.length; ii++) {
            for (int jj = 0; jj < model[ii].length; jj++) {
                if (model[ii][jj] == ModelId.Trace) {
                    base[ii][jj] = trace;
                }
                if (model[ii][jj] == ModelId.Via) {
                    for (int ox = -viaPixelWidth / 2; ox < viaPixelWidth / 2; ox++) {
                        for (int oy = -viaPixelWidth / 2; oy < viaPixelWidth / 2; oy++) {
                            int x = ii + ox;
                            int y = jj + oy;
                            if (x >= 0 && y >= 0 && x < base.length && y < base[0].length) {
                                base[x][y] = via;
                            }
                        }
                    }
                }
            }
        }

        return SpriteMaker.get().fromColors(base);
    }

    public static Sprite generate() {
        return convertModelToTexture(buildModel((int) Screen.width(100), (int) Screen.height(100)));
    }
}
