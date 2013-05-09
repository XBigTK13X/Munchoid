package game;

import sps.core.Point2;
import sps.util.Screen;

public class GameConfig {
    public final static int CreatureLimit = 5;
    public final static int SpacePercentPerCreature = 1;
    public final static float ArenaTimeoutSeconds = 1f;
    public static final float ArenaCreatureScale = .4f;
    public static final int playerTopSpeed = 500;
    public static final int playerAcceleration = 50;

    public final static int TournamentMatches = 3;

    public static final Point2 MinBodyPartSize = Screen.pos(5, 5);
    public static final Point2 MaxBodyPartSize = Screen.pos(13, 13);
    public static final int MinBodyParts = 4;
    public static final int MaxBodyParts = 6;

    public static final int MinStatInit = 2;
    public static final int MaxStatInit = 5;

    public static final int MinMergeImpactPercent = 15;
    public static final int MaxMergeImpactPercent = 40;

    public static final int BonusAmount = 1;
    public static final int BonusAward = 15;

    public static final int DisableStat = 0;
    public static final int MinStat = 0;
    public static final int MaxStat = 100;

    public static final int SliceScale = 4;
    public static final int AbrasiveScale = 10;
    public static final int ExplosiveScale = 15;
    public static final int ContractionScale = 3;
    public static final int VaporizeScale = 10;
    public static final int ExpansionScale = 4;

    public static final float MinScaleDeath = .5f;
    public static final float MaxScaleDeath = 1.5f;

    public static final float ForceColorIntensity = .85f;
    public static final float ForceColorMix = .4f;
}
