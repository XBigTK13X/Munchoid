package game.creatures.part;

import game.creatures.PartFunction;
import org.apache.commons.io.FileUtils;
import sps.core.Loader;
import sps.core.Logger;
import sps.display.Screen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class DesignSilhouette implements Design {
    public List<PartFunction> Functions;
    private boolean[][] _silhouette;

    public DesignSilhouette(File details) {
        try {
            boolean firstLine = true;
            Functions = new LinkedList<>();
            File silhouette = null;
            for (String line : FileUtils.readLines(details)) {
                if (firstLine) {
                    silhouette = Loader.get().graphics("designs/" + line.trim());
                    firstLine = false;
                }
                else {
                    String[] rawFunctions = line.split(",");
                    for (String function : rawFunctions) {
                        Functions.add(PartFunction.lookup(function.trim()));
                    }
                }
            }
            buildSilhouette(silhouette.getAbsolutePath());
        }
        catch (Exception e) {
            Logger.exception(e);
        }
    }

    public DesignSilhouette(final String rawGraphicsPath) {
        buildSilhouette(rawGraphicsPath);
    }

    public void buildSilhouette(final String filePath) {
        try {
            File silhouette = new File(filePath);
            BufferedImage image = ImageIO.read(silhouette);
            _silhouette = new boolean[image.getWidth()][image.getHeight()];
            for (int ii = 0; ii < image.getWidth(); ii++) {
                for (int jj = 0; jj < image.getHeight(); jj++) {
                    int c = image.getRGB(ii, jj);
                    _silhouette[ii][jj] = c <= 0;
                }
            }
        }
        catch (Exception e) {
            Logger.exception(e);
        }
    }

    @Override
    public int[][] create(int width, int height) {

        int sw = _silhouette.length;
        int sh = _silhouette[0].length;
        float origAspect = (float) sw / sh;

        float newAspect = (float) width / (float) height;
        float scale = (float) width / sw;

        if (newAspect > origAspect) {
            scale = (float) height / sh;
        }
        else if (newAspect < origAspect) {
            scale = (float) width / sw;
        }

        float w = (float) sw * scale;
        float h = (float) sh * scale;

        int[][] result = new int[width][height];
        for (int ii = 0; ii < w; ii++) {
            for (int jj = 0; jj < h; jj++) {
                int ip = (int) (sw * ((float) ii / w));
                int jp = (int) (sh * ((float) jj / h));
                result[ii][jj] = _silhouette[ip][jp] ? Design.BaseColor : Design.Empty;
            }
        }
        return result;
    }
}
