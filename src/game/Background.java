package game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.core.Logger;
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

    private static final int modelScale = 6;
    private static final int viaPixelWidth = 10;

    private static ModelId[][] buildModel(int width, int height) {
        List<Point2> vias = new ArrayList<Point2>();

        width = width / modelScale;
        height = height / modelScale;
        ModelId[][] result = new ModelId[width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                result[ii][jj] = ModelId.Empty;
            }
        }

        //Via placement
        int viaCount = RNG.next(width / 10, width / 8) + RNG.next(height / 10, height / 8);
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
                vias.remove(end);
                vias.remove(start);
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
        }

        return result;
    }

    private static Sprite convertModelToTexture(ModelId[][] model) {
        Color[][] base = ProcTextures.monotone((int) Screen.width(100), (int) Screen.height(100), Colors.shade(Colors.randomPleasant(), -80));
        Color via = Colors.randomPleasant();
        Color trace = Colors.randomPleasant();

        for (int ii = 0; ii < model.length; ii++) {
            for (int jj = 0; jj < model[ii].length; jj++) {
                if (model[ii][jj] == ModelId.Via) {
                    for (int ox = -viaPixelWidth / 2; ox < viaPixelWidth / 2; ox++) {
                        for (int oy = -viaPixelWidth / 2; oy < viaPixelWidth / 2; oy++) {
                            int x = ii * modelScale + ox;
                            int y = jj * modelScale + oy;
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
