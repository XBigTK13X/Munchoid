package sps.util;

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
}
