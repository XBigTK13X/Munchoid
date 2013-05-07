package game.creatures;

import sps.core.RNG;

import java.util.ArrayList;
import java.util.List;

public class Merge {
    public static Creature two(Creature a, Creature b) {
        List<BodyPart> pool = new ArrayList<BodyPart>();
        pool.addAll(a.getBody().getParts());
        pool.addAll(b.getBody().getParts());
        int partMean = pool.size() / 2;

        //TODO Remove both bodies first and choose one
        while (pool.size() > partMean) {
            pool.remove(RNG.next(0, pool.size()));
        }
        Creature result = new Creature(new Body(pool));

        return result;
    }
}
