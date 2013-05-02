package sps.util;

import com.badlogic.gdx.graphics.Color;
import sps.graphics.Renderer;

import static org.lwjgl.opengl.GL11.*;

public class RawPixels {
    private static RawPixels __instance;

    public static RawPixels get() {
        if (__instance == null) {
            __instance = new RawPixels();
        }
        return __instance;
    }


    private Color[][] _pixels;

    private RawPixels() {
        _pixels = new Color[Renderer.get().VirtualWidth][Renderer.get().VirtualHeight];
    }

    public void draw() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Renderer.get().VirtualWidth, Renderer.get().VirtualHeight, 0, 0, 1);
        glDisable(GL_DEPTH_TEST);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glTranslatef(0.375f, 0.375f, 0);
        glBegin(GL_POINTS);

        for (int ii = 0; ii < Renderer.get().VirtualWidth; ii++) {
            for (int jj = 0; jj < Renderer.get().VirtualHeight; jj++) {
                if (_pixels[ii][jj] != null) {
                    glColor3f(_pixels[ii][jj].r, _pixels[ii][jj].g, _pixels[ii][jj].b);
                    glVertex2f(ii, jj);
                    _pixels[ii][jj] = null;
                }
            }
        }

        glEnd();
    }

    public void setPixel(int x, int y, Color color) {
        if (x > 0 && y > 0 && x < _pixels.length && y < _pixels[0].length) {
            _pixels[x][_pixels[0].length - y] = color;
        }
    }
}
