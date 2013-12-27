package game.creatures.part;

import game.creatures.Atom;
import game.creatures.PartFunction;
import game.creatures.part.designimpls.*;
import org.apache.commons.io.FilenameUtils;
import sps.color.Color;
import sps.color.Colors;
import sps.core.Loader;
import sps.core.Logger;
import sps.core.RNG;
import sps.draw.ProcTextures;

import java.io.File;
import java.util.*;

public class Designs {
    private static final Map<PartFunction, List<Design>> __designs;

    static {
        Map<PartFunction, List<Design>> tmp = new HashMap<PartFunction, List<Design>>();
        for (PartFunction function : PartFunction.values()) {
            tmp.put(function, new ArrayList<Design>());
        }
        tmp.get(PartFunction.Head).add(new RoundHead());
        tmp.get(PartFunction.Head).add(new RegularPoly());
        tmp.get(PartFunction.HeadDetail).add(new RoundEye());
        tmp.get(PartFunction.UpperLimb).add(new LimbSegment());
        tmp.get(PartFunction.UpperLimb).add(new RegularPoly());
        tmp.get(PartFunction.Core).add(new RectangleBody());
        tmp.get(PartFunction.LowerLimb).add(new LimbSegment());
        tmp.get(PartFunction.LowerLimb).add(new RegularPoly());

        //Silhouettes that are restricted to a subset of partfunctions
        File silhouetteDesigns = Loader.get().graphics("designs");
        for (File file : silhouetteDesigns.listFiles()) {
            if (FilenameUtils.getExtension(file.getName()).equals("prt")) {
                DesignSilhouette silhouette = new DesignSilhouette(file);
                for (PartFunction function : silhouette.Functions) {
                    tmp.get(function).add(silhouette);
                }
            }
        }

        //Silhouettes that can be any partfunction
        File rawSilhouettes = Loader.get().graphics("designs/raw");
        for (File file : rawSilhouettes.listFiles()) {
            if (FilenameUtils.getExtension(file.getName()).equals("png")) {
                DesignSilhouette silhouette = new DesignSilhouette(file.getAbsolutePath());
                for (PartFunction function : PartFunction.values()) {
                    if (function != PartFunction.Core) {
                        tmp.get(function).add(silhouette);
                    }
                }
            }
        }

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

    public static Color[][] toColors(int[][] design, Color color) {
        design = Common.trim(design);
        int width = design.length;
        int height = design[0].length;
        Color[][] textureBase = Designs.getTexture(width, height, color);
        Color[][] result = new Color[width][height];
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
                    result[ii][jj] = c;
                }
            }
        }
        return result;
    }

    public static Color[][] getTexture(int width, int height, Color color) {
        return ProcTextures.perlin(width, height, Colors.darken(color), Colors.lighten(color));
    }
}
