package game.creatures.style;

import game.creatures.BodyPart;
import game.creatures.PartFunction;
import sps.core.RNG;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BodyRules {
    public static Map<PartFunction, List<PartFunction>> __requirements = new HashMap<PartFunction, List<PartFunction>>();
    public static Map<PartFunction, List<PartFunction>> __supports = new HashMap<PartFunction, List<PartFunction>>();

    static {
        List<PartFunction> core = Arrays.asList(PartFunction.Body);
        __requirements.put(PartFunction.Body, null);
        __requirements.put(PartFunction.BodyDetail, core);
        __requirements.put(PartFunction.Head, core);
        __requirements.put(PartFunction.HeadDetail, Arrays.asList(PartFunction.Head));
        __requirements.put(PartFunction.LowerLimb, core);
        __requirements.put(PartFunction.UpperLimb, core);

        __supports.put(PartFunction.Body, Arrays.asList(PartFunction.BodyDetail, PartFunction.UpperLimb, PartFunction.LowerLimb, PartFunction.Head));
        __supports.put(PartFunction.Head, Arrays.asList(PartFunction.HeadDetail));
    }

    public static PartFunction getSupported(List<BodyPart> parts) {
        if (parts.size() == 0) {
            return PartFunction.Body;
        }
        while (true) {
            PartFunction target = parts.get(RNG.next(0, parts.size())).getFunction();
            if (__supports.containsKey(target)) {
                return __supports.get(target).get(RNG.next(0, __supports.get(target).size()));
            }
        }
    }
}
