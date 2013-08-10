package game.creatures;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import game.forces.Force;
import sps.core.RNG;
import sps.draw.Colors;
import sps.draw.HSV;

public class Merge {
    public static Creature creatures(Creature a, Creature b) {
        Color mergedColor = HSV.average(a.getBody().getColor(), b.getBody().getColor());

        Creature result = new Creature(new Body(mergedColor));

        result.setStats(stats(a.getStats(), b.getStats()));

        //TODO MergeOutcome in size as well as stats/body
        return result;
    }

    public static Stats stats(Stats a, Stats b) {
        Stats result = new Stats();
        for (Force force : Force.values()) {
            int average = (a.get(force) + b.get(force)) / 2;
            int impact = (int) (average * (RNG.next(GameConfig.MinMergeImpactPercent, GameConfig.MaxMergeImpactPercent) / 100f));
            if (impact == 0 && b.get(force) > 0) {
                impact = 1;
            }

            result.set(force, a.get(force) + impact);
            result.setEnabled(force, result.canBeEnabled(force));
        }
        return result;
    }

    public static Stats rejectStats(Stats a) {
        Stats result = new Stats(a);
        Force target = a.randomEnablePossible();
        int growth = (int) (Math.ceil(a.get(target) * (GameConfig.MergeRejectGrowthPercent / 100f)));
        result.set(target, a.get(target) + growth);

        for (Force force : Force.values()) {
            result.setEnabled(force, result.canBeEnabled(force));
        }
        return result;
    }
}
