package game.creatures.part;

import com.badlogic.gdx.graphics.Color;
import game.creatures.Atom;
import game.creatures.PartFunction;
import sps.core.RNG;
import sps.util.Colors;

import java.util.*;

public class Designs {
    private static final Map<PartFunction, List<Design>> __designs;

    static {
        Map<PartFunction, List<Design>> tmp = new HashMap<PartFunction, List<Design>>();
        for (PartFunction function : PartFunction.values()) {
            tmp.put(function, new ArrayList<Design>());
        }
        tmp.get(PartFunction.Head).add(new RoundHead());
        tmp.get(PartFunction.HeadDetail).add(new RoundEye());
        tmp.get(PartFunction.UpperLimb).add(new QuadArm());
        tmp.get(PartFunction.BodyDetail).add(new PolyAccent());
        tmp.get(PartFunction.Body).add(new RoundedRectangleBody());
        tmp.get(PartFunction.Body).add(new StarFishBody());
        tmp.get(PartFunction.LowerLimb).add(new QuadLeg());
        __designs = Collections.unmodifiableMap(tmp);
    }

    public static Design get(PartFunction function) {
        return __designs.get(function).get(RNG.next(0, __designs.get(function).size()));
    }

    public static Atom[][] toAtoms(int[][] design, Color color) {
        design = Common.trim(design);
        int width = design.length;
        int height = design[0].length;
        Color[][] textureBase = Designs.getTexture(width, height, color);
        Atom[][] result = new Atom[width][height];
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                if (design[ii][jj] != Design.Empty) {
                    Color c = textureBase[ii][jj];
                    if (design[ii][jj] == Design.White) {
                        c = Color.WHITE;
                    }
                    if (design[ii][jj] == Design.Black) {
                        c = Color.BLACK;
                    }
                    result[ii][jj] = new Atom(ii, jj, c);
                }
            }
        }
        return result;
    }

    public static Color[][] getTexture(int width, int height, Color color) {
        return Colors.genPerlinGrid(width, height, Colors.darken(color), Colors.lighten(color));
    }
}
