package game.forces;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import sps.core.RNG;
import sps.util.Colors;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum Force {

    Abrasive(Colors.rgbToColor(GameConfig.ForceColorIntensity, GameConfig.ForceColorMix, GameConfig.ForceColorMix)),
    Expansion(Colors.rgbToColor(GameConfig.ForceColorMix, GameConfig.ForceColorIntensity, GameConfig.ForceColorMix)),
    Explosive(Colors.rgbToColor(GameConfig.ForceColorMix, GameConfig.ForceColorMix, GameConfig.ForceColorIntensity)),
    Slice(Colors.rgbToColor(GameConfig.ForceColorIntensity, GameConfig.ForceColorIntensity, GameConfig.ForceColorMix)),
    Contraction(Colors.rgbToColor(GameConfig.ForceColorIntensity, GameConfig.ForceColorMix, GameConfig.ForceColorIntensity)),
    Vaporize(Colors.rgbToColor(GameConfig.ForceColorMix, GameConfig.ForceColorIntensity, GameConfig.ForceColorIntensity));

    public final String Command;
    public final Color Color;

    private Force(Color color) {
        Command = "Force" + (ordinal() + 1);
        Color = color;
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

    public static Force strength(Force force) {
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

    public static Force weakness(Force force) {
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

    public static BaseForce createRandom(int magnitude) {
        return create(Force.values()[RNG.next(0, Force.values().length)], magnitude);
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
