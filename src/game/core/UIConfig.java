package game.core;

import sps.core.Point2;
import sps.display.Screen;

public class UIConfig {
    //Population - Tournament
    public static final boolean UseOldPopulationDeathMonitors = false;

    public static Point2 PopulationHUDSize() {
        return Screen.pos(40, 70);
    }

    public static Point2 PopulationHUDPosition() {
        return Screen.pos(10, 15);
    }

    public static Point2 PopulationContinuePosition() {
        return Screen.pos(30, 10);
    }

    public static Point2 PopulationCountPosition() {
        return Screen.pos(15, 95);
    }

    public static Point2 PopulationSolutionMeterPosition() {
        return Screen.pos(55, 80);
    }

    public static Point2 PopulationSolutionsCaptionPosition() {
        return Screen.pos(55, 90);
    }

    public static Point2 PopulationPlayByPlaySize() {
        return Screen.pos(40, 59);
    }

    public static Point2 PopulationPlayByPlayPosition() {
        return Screen.pos(55, 15);
    }

    //Arena
    public static final boolean ArenaShowCreatureCount = false;
    public static final boolean EnableChompText = false;
    public static final int SpacePercentPerCreature = 1;
    public static final float ArenaCreatureScale = .6f;

    public static final int ArenaBufferPercent() {
        return (int) ((GameConfig.CreatureLimit / 2) * UIConfig.SpacePercentPerCreature);
    }

    public static int ArenaHeight() {
        return Screen.get().VirtualHeight + (int) Screen.height(ArenaBufferPercent()) * 2;
    }

    public static int ArenaWidth() {
        return Screen.get().VirtualWidth + (int) Screen.width(ArenaBufferPercent()) * 2;
    }

    //Battle
    public static boolean BattleEnableEnemyForcesHUD = false;
    public static final Point2 BattleEnemyPosition = Screen.pos(60, 35);
    public static final Point2 BattlePlayerPosition = Screen.pos(25, 35);
    public static final Point2 BattlePlayerForcesHUDPosition = Screen.pos(15, 85);
    public static final Point2 BattleEnemyForcesHUDPosition = Screen.pos(55, 85);
    public static final boolean BattleEffectHUDEnabled = false;
    public static final float BattleCreatureScale = 1f;
    public static final float ForceColorIntensity = .85f;
    public static final float ForceColorMix = .4f;
}
