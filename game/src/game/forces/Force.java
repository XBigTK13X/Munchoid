package game.forces;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import game.forces.sideeffects.*;
import sps.core.Logger;
import sps.core.RNG;
import sps.draw.Colors;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum Force {
    Abrasive(0, 1, 1, 1),
    Expansion(1, 0, 1, 4),
    Explosive(1, 1, 0, 2),
    Slice(0, 0, 1, 5),
    Contraction(0, 1, 0, 3),
    Vaporize(1, 0, 0, 6);

    public final String Command;
    public final Color Color;

    private Force(float rT, float gT, float bT, int inputId) {
        Command = "Force" + inputId;
        Color = Colors.fromRGB(tToC(rT), tToC(gT), tToC(bT));
        Logger.info(name() + ": " + Color.r + "," + Color.g + "," + Color.b);
    }

    private static float tToC(float t) {
        return t == 0 ? GameConfig.ForceColorIntensity : GameConfig.ForceColorMix;
    }

    public static BaseForce create(Force force, int magnitude) {
        switch (force) {
            case Abrasive:
                return new Abrasive(magnitude);
            case Expansion:
                return new Expansion(magnitude);
            case Explosive:
                return new Explosive(magnitude);
            case Slice:
                return new Slice(magnitude);
            case Contraction:
                return new Contraction(magnitude);
            case Vaporize:
                return new Vaporize(magnitude);
        }
        return null;
    }

    public static Force beats(Force force) {
        switch (force) {
            case Abrasive:
                return Force.Vaporize;
            case Expansion:
                return Force.Abrasive;
            case Explosive:
                return Force.Slice;
            case Slice:
                return Force.Contraction;
            case Contraction:
                return Force.Expansion;
            case Vaporize:
                return Force.Explosive;
        }
        return null;
    }

    public static Force beatenBy(Force force) {
        switch (force) {
            case Abrasive:
                return Force.Slice;
            case Expansion:
                return Force.Explosive;
            case Explosive:
                return Force.Expansion;
            case Slice:
                return Force.Abrasive;
            case Contraction:
                return Force.Vaporize;
            case Vaporize:
                return Force.Contraction;
        }
        return null;
    }

    public static SideEffect sideEffect(Force force) {
        switch (force) {
            case Abrasive:
                return new WeakFlesh();
            case Expansion:
                return new Growth();
            case Explosive:
                return new Burn();
            case Slice:
                return new Stun();
            case Contraction:
                return new Quickening();
            case Vaporize:
                return new Ethereal();
        }
        return null;
    }

    public static Force random() {
        return values()[RNG.next(0, values().length)];
    }

    public static Force[] random(int amount) {
        if (amount > values().length) {
            amount = values().length;
        }
        Force[] result = new Force[amount];
        List<Force> v = new LinkedList<Force>(Arrays.asList(values()));
        while (amount > 0) {
            result[amount - 1] = v.remove(RNG.next(0, v.size()));
            amount--;
        }
        return result;
    }
}
