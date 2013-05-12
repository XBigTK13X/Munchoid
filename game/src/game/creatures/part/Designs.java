package game.creatures.part;

import game.creatures.PartFunction;
import sps.core.RNG;

import java.util.*;

public class Designs {
    private static final Map<PartFunction, List<Design>> __designs;

    static {
        Map<PartFunction, List<Design>> tmp = new HashMap<PartFunction, List<Design>>();
        for (PartFunction function : PartFunction.values()) {
            tmp.put(function, new ArrayList<Design>());
        }
        tmp.get(PartFunction.Head).add(new RoundHead());
        tmp.get(PartFunction.HeadDetail).add(new RoundEye());
        tmp.get(PartFunction.UpperLimb).add(new QuadArm());
        tmp.get(PartFunction.BodyDetail).add(new PolyAccent());
        tmp.get(PartFunction.Body).add(new RoundedRectangleBody());
        tmp.get(PartFunction.Body).add(new StarFishBody());
        tmp.get(PartFunction.LowerLimb).add(new QuadLeg());
        __designs = Collections.unmodifiableMap(tmp);
    }

    public static Design get(PartFunction function) {
        return __designs.get(function).get(RNG.next(0, __designs.get(function).size()));
    }
}
