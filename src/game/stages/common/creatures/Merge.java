package game.stages.common.creatures;

import game.app.config.GameConfig;
import game.stages.common.creatures.style.Grid;
import game.stages.common.forces.Force;
import sps.color.Color;
import sps.color.HSV;
import sps.core.RNG;

import java.util.ArrayList;
import java.util.List;

public class Merge {
    public static Creature creatures(Creature a, Creature b) {
        Creature result = new Creature(body(a, b));

        result.setStats(stats(a.getStats(), b.getStats()));

        //TODO MergeOutcome in size as well as stats/body
        return result;
    }

    //FIXME This seems way more complicated than it needs to be
    public static Body body(Creature a, Creature b) {
        Color mergedColor = HSV.fromColor(a.getBody().getColor()).average(HSV.fromColor(b.getBody().getColor())).toColor();

        BodyParts ab = a.getBody().getParts();
        BodyParts bb = b.getBody().getParts();
        BodyPart abc = ab.getCore();
        BodyPart bbc = bb.getCore();
        List<Joint> coreJoints = new ArrayList<>();
        Joints abcj = abc.getJoints();
        Joints bbcj = bbc.getJoints();
        BodyPart core = RNG.coinFlip() ? abc : bbc;

        for (int ii = 1; ii <= Grid.JointSlots; ii++) {
            Joint j = abcj.get(ii);
            Joint j2 = bbcj.get(ii);
            if (j != null && j2 != null) {
                if (j.getChild() != null && j2.getChild() != null) {
                    coreJoints.add(RNG.coinFlip() ? j : j2);
                }
                else if (j.getChild() != null) {
                    if (RNG.coinFlip()) {
                        coreJoints.add(j);
                    }
                }
                else if (j2.getChild() != null) {
                    if (RNG.coinFlip()) {
                        coreJoints.add(j2);
                    }
                }
            }
        }

        //TODO Walk joint branches during copy

        List<BodyPart> parts = new ArrayList<>();
        BodyPart freshCore = new BodyPart(core, null, mergedColor);
        parts.add(freshCore);
        for (Joint base : coreJoints) {
            walkCopy(parts, freshCore, freshCore, base, mergedColor, null);
        }

        Body result = new Body(mergedColor, parts);
        return result;
    }

    private static void walkCopy(List<BodyPart> parts, BodyPart core, BodyPart parent, Joint joint, Color color, BodyPart originalParent) {
        if (core == parent) {
            BodyPart freshChild = new BodyPart(joint.getChild(), null, color);
            freshChild.setParent(core, core.getJoints().get(joint.GridLoc));
            parts.add(freshChild);
            walkCopy(parts, core, freshChild, joint, color, joint.getChild());
        }
        else {
            for (Joint j : originalParent.getJoints().getAll()) {
                if (j.getChild() != null) {
                    BodyPart freshChild = new BodyPart(j.getChild(), null, color);
                    freshChild.setParent(parent, parent.getJoints().get(j.GridLoc));
                    parts.add(freshChild);
                    walkCopy(parts, core, freshChild, null, color, j.getChild());
                }
            }
        }
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
