package game;

import sps.core.Point2;
import sps.display.Screen;

public class GameConfig {
    //Options / Optimizations
    //TODO Make these toggleable in the options menu,
    //     helps with lots of creatures running on weaker computers
    public static boolean OptPerformanceGraphicsSettings = false;
    public static final boolean OptCollectMetaData = true;
    public static boolean OptDisableCloudyTextures = false;
    public static boolean OptSimpleBackgrounds = false;
    public static boolean OptEnableFontOutlines = true;
    public static boolean OptCreatureOutlineEnabled = true;
    public static final boolean OptShowFPS = false;
    public static final boolean OptSmoothRegionMap = true;
    public static final boolean OptDrawBones = false;
    public static final boolean OptArenaPCBBackground = true;
    public static float OptGraphicsDetailScale = 1f;

    public static void setGraphicsMode(boolean performanceMode) {
        OptPerformanceGraphicsSettings = performanceMode;
        //OptCreatureOutlineEnabled = !performanceMode;
        BodyPartOutlinePixelThickness = performanceMode ? 2 : 5;
        OptSimpleBackgrounds = performanceMode;
        OptGraphicsDetailScale = performanceMode ? .5f : 1f;
    }

    //Intro
    public static final float IntroVideoSkipSeconds = 2f;
    public static final boolean IntroVideoEnabled = false;

    //Arena
    public static final int CreatureLimit = 50;
    public static final int SpacePercentPerCreature = 1;
    public static final int ArenaBufferPercent = (int) ((GameConfig.CreatureLimit / 2) * GameConfig.SpacePercentPerCreature);

    public static int ArenaHeight() {
        return Screen.get().VirtualHeight + (int) Screen.height(ArenaBufferPercent) * 2;
    }

    public static int ArenaWidth() {
        return Screen.get().VirtualWidth + (int) Screen.width(ArenaBufferPercent) * 2;
    }

    //The length of Quickly.ogg
    public static final float ArenaTimeoutSeconds = 9f;
    public static final float ArenaCreatureScale = .6f;
    public static final boolean EnableChompText = false;

    public static final float PlayerFrozenSecondsMax = .5f;
    public static final int ArenaMergeChance = 60;
    //TODO Convert into easier to manipulate units
    public static final int PlayerTopSpeed = 500;

    public static final int ChompRewardStatsImpact = 2;
    public static final int ChompPointsRewardCost = Math.max(GameConfig.CreatureLimit / 10, 1);
    public static final int ChompPoints = 1;

    //Creature construction
    public static final Point2 MinBodyPartSize = Screen.pos(7, 7);
    public static final Point2 MaxBodyPartSize = Screen.pos(13, 13);
    public static final int MinBodyParts = 3;
    public static final int MaxBodyParts = 10;
    public static final int InitEnabledStats = 1;
    public static final int MinStatInit = 2;
    public static final int MaxStatInit = 5;
    public static final int DisableStat = 0;

    //Merging
    public static final int MinMergeImpactPercent = 15;
    public static final int MaxMergeImpactPercent = 30;
    public static final int MergeRejectGrowthPercent = 40;
    public static final int NaturalStatGrowthPercent = 10;


    //Battle
    public static boolean BattleEnableEnemyForcesHUD = false;
    public static final Point2 BattleEnemyPosition = Screen.pos(60, 35);
    public static final Point2 BattlePlayerPosition = Screen.pos(25, 35);
    public static final Point2 BattlePlayerForcesHUDPosition = Screen.pos(15, 85);
    public static final Point2 BattleEnemyForcesHUDPosition = Screen.pos(55, 85);
    public static final boolean BattleEffectHUDEnabled = false;
    public static final float BattleCreatureScale = 1f;
    public static final float FirstFightMult = 1.5f;
    public static final int MaxForcesEnabled = 4;
    public static final int MinStat = 0;
    public static final int MaxStat = 100;
    public static final float ForceColorIntensity = .85f;
    public static final float ForceColorMix = .4f;
    public static final int SliceScale = 6;
    public static final int AbrasiveScale = 6;
    public static final int ExplosiveScale = 10;
    public static final int ContractionScale = 3;
    public static final int VaporizeScale = 15;
    public static final int ExpansionScale = 6;
    public static final int MinInitCooldownMilliSeconds = 500;
    public static final int MaxInitCooldownMilliSeconds = 800;
    public static final float MinCoolDown = .1f;
    public static final float MinScaleDeath = .5f;
    public static final float MaxScaleDeath = 1.5f;

    //Tournament/Population
    public static final int TournamentStatRange = 10;
    public static final int NumberOfTournaments = 5;
    public static final int TournamentMatches = 3;
    public static final int StartingPopulationSize = 1_000_000;
    public static final int NaturalPopulationGrowthPercent = 27;
    public static final int PopulationMax = 70_000_000;

    //Style
    //TODO Make this scale with resolution like everything else?
    public static final int MeterOutlinePixelThickness = 2;
    public static int BodyPartOutlinePixelThickness = 5;
}
