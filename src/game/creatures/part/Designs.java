package game.creatures.part;

import game.core.GameConfig;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Designs {
    private static Map<PartFunction, List<Design>> __designs;

    public static boolean indexExists() {
        return __designs != null;
    }

    public static void rebuildIndex() {
        __designs = new HashMap<PartFunction, List<Design>>();
        for (PartFunction function : PartFunction.values()) {
            __designs.put(function, new ArrayList<Design>());
        }
        __designs.get(PartFunction.Eye).add(new RoundEye());
        __designs.get(PartFunction.Head).add(new RoundHead());

        if (GameConfig.OptProceduralBodyPartDesignsEnabled) {
            __designs.get(PartFunction.Head).add(new RegularPoly());
            __designs.get(PartFunction.UpperLimb).add(new LimbSegment());
            __designs.get(PartFunction.UpperLimb).add(new RegularPoly());
            __designs.get(PartFunction.Core).add(new RectangleBody());
            __designs.get(PartFunction.LowerLimb).add(new LimbSegment());
            __designs.get(PartFunction.LowerLimb).add(new RegularPoly());
        }

        //Silhouettes that have explicit detailed part functions
        File silhouetteDesigns = Loader.get().graphics("designs");
        for (File file : silhouetteDesigns.listFiles()) {
            if (file.isDirectory() && !file.getName().equals("raw")) {
                for (File specSource : file.listFiles()) {
                    if (FilenameUtils.getExtension(specSource.getName()).equals("partspec")) {
                        try {
                            SilhouetteSpecs spec = new SilhouetteSpecs(specSource);
                            for (File image : spec.getEntries().keySet()) {
                                try {
                                    SilhouetteDesign silhouette = new SilhouetteDesign(image);
                                    for (PartFunction function : spec.getEntries().get(image)) {
                                        __designs.get(function).add(silhouette);
                                    }
                                }
                                catch (Exception e) {
                                    Logger.error("Unable to load part silhouette: " + image.getAbsolutePath());
                                }
                            }
                        }
                        catch (Exception e) {
                            Logger.error("Invalid .prt format. Unable to read silhouette spec: " + specSource);
                        }
                    }
                }
            }
        }

        //Silhouettes that can be any partfunction (except core)
        File rawSilhouettes = Loader.get().graphics("designs/raw");
        for (File file : rawSilhouettes.listFiles()) {
            if (file.getName().charAt(0) != '.' && !file.getName().contains("~")) {
                try {
                    SilhouetteDesign silhouette = new SilhouetteDesign(file);
                    for (PartFunction function : PartFunction.values()) {
                        if (function != PartFunction.Core) {
                            __designs.get(function).add(silhouette);
                        }
                    }
                }
                catch (Exception e) {
                    Logger.error("Invalid image format in RAW silhouettes: " + file.getAbsolutePath());
                }
            }
        }
    }

    public static Design get(PartFunction function) {
        if (!Designs.indexExists()) {
            PartFunction.initJointSpecs();
            Designs.rebuildIndex();
        }
        return RNG.pick(__designs.get(function));
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
