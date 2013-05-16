package sps.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.creatures.Atom;

public class SpriteMaker {
    private static SpriteMaker __instance;

    public static SpriteMaker get() {
        if (__instance == null) {
            __instance = new SpriteMaker();
        }
        return __instance;
    }

    private SpriteMaker() {

    }

    Atom at;

    public Sprite fromAtoms(Atom[][] atoms) {
        Pixmap textureBase = new Pixmap(atoms.length, atoms[0].length, Pixmap.Format.RGBA8888);
        for (int ii = 0; ii < atoms.length; ii++) {
            for (int jj = 0; jj < atoms[0].length; jj++) {
                at = atoms[ii][jj];
                if (at != null && at.isActive()) {
                    textureBase.setColor(at.getColor());
                    textureBase.drawPixel((int) at.getLocalX(), atoms[0].length - (int) at.getLocalY());
                }
            }
        }
        return new Sprite(new Texture(textureBase));
    }

    Color co;

    public Sprite fromColors(Color[][] colors) {
        Pixmap textureBase = new Pixmap(colors.length, colors[0].length, Pixmap.Format.RGBA8888);
        for (int ii = 0; ii < colors.length; ii++) {
            for (int jj = 0; jj < colors[0].length; jj++) {
                co = colors[ii][jj];
                if (co != null) {
                    textureBase.setColor(co);
                    textureBase.drawPixel(ii, colors[0].length - jj);
                }
            }
        }
        return new Sprite(new Texture(textureBase));
    }

    public Sprite pixel(Color color) {
        Pixmap textureBase = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        textureBase.setColor(color);
        textureBase.drawPixel(0, 0);
        return new Sprite(new Texture(textureBase));
    }
}
