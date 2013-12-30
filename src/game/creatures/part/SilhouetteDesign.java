package game.creatures.part;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SilhouetteDesign implements Design {
    private boolean[][] _silhouette;
    private File _source;

    public SilhouetteDesign(final File source) throws IOException {
        _source = source;
        BufferedImage image = ImageIO.read(_source);
        _silhouette = new boolean[image.getWidth()][image.getHeight()];

        for (int ii = 0; ii < image.getWidth(); ii++) {
            for (int jj = 0; jj < image.getHeight(); jj++) {
                int color = image.getRGB(ii, jj);
                int r = (color & 0x00ff0000) >> 16;
                int g = (color & 0x0000ff00) >> 8;
                int b = color & 0x000000ff;
                boolean enabled = r <= 1 && g <= 1 && b <= 1;
                _silhouette[ii][jj] = enabled;
            }
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

        int w = (int) ((float) sw * scale);
        int h = (int) ((float) sh * scale);

        int[][] result = new int[w][h];
        for (int ii = 0; ii < w; ii++) {
            for (int jj = 0; jj < h; jj++) {
                int ip = (int) (sw * ((float) ii / w));
                int jp = (int) (sh * ((float) jj / h));
                int color = _silhouette[ip][sh - jp - 1] ? Design.BaseColor : Design.Empty;
                result[ii][jj] = color;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return _source.getName();
    }
}
