package game.creatures;

import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import game.forces.Force;
import sps.core.RNG;
import sps.util.Colors;

import java.util.ArrayList;
import java.util.List;

public class Merge {
    public static Creature two(Creature a, Creature b) {
        //Create a new body from the two inputs
        List<BodyPart> pool = new ArrayList<BodyPart>();
        pool.addAll(a.getBody().getParts());
        pool.addAll(b.getBody().getParts());

        List<BodyPart> bodies = new ArrayList<BodyPart>();
        for (int ii = 0; ii < pool.size(); ii++) {
            if (pool.get(ii).getFunction() == PartFunction.Body) {
                bodies.add(pool.remove(ii));
            }
        }

        int partMean = pool.size() / 2;
        Color mergedColor = Colors.hsvAverage(a.getBody().getColor(), b.getBody().getColor());

        while (pool.size() > partMean) {
            pool.remove(RNG.next(0, pool.size()));
        }

        pool.add(0, bodies.get(RNG.next(0, bodies.size())));
        Creature result = new Creature(new Body(pool, mergedColor));

        //Average the stats together
        for (Force force : Force.values()) {
            int average = (a.getStats().get(force) + b.getStats().get(force)) / 2;
            int impact = (int) (average * (RNG.next(GameConfig.MinMergeImpactPercent, GameConfig.MaxMergeImpactPercent) / 100f));
            if (impact == 0 && b.getStats().get(force) > 0) {
                impact = 1;
            }

            result.getStats().set(force, a.getStats().get(force) + impact);
            result.getStats().setEnabled(force, a.getStats().isEnabled(force));
        }

        //TODO MergeOutcome in size as well as stats/body
        return result;
    }
}
