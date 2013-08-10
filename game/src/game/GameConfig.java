package game;

import sps.core.Logger;
import sps.core.Point2;
import sps.display.Screen;

import java.lang.reflect.Field;

public class GameConfig {

    public final static float IntroVideoSkipSeconds = 2f;
    public final static boolean IntroVideoEnabled = false;

    public final static int CreatureLimit = 50;
    public final static int TournamentMatches = 3;

    public final static int SpacePercentPerCreature = 1;
    public static final int ArenaBufferPercent = (int) ((GameConfig.CreatureLimit / 2) * GameConfig.SpacePercentPerCreature);
    public static final int ArenaHeight = Screen.get().VirtualHeight + (int) Screen.height(ArenaBufferPercent) * 2;
    public static final int ArenaWidth = Screen.get().VirtualWidth + (int) Screen.width(ArenaBufferPercent) * 2;
    //The length of Quickly.ogg
    public final static float ArenaTimeoutSeconds = 10f;
    public static final float ArenaCreatureScale = .6f;
    public static final float BattleCreatureScale = 1f;
    public static final float PlayerFrozenSecondsMax = .5f;
    public static final int ArenaMergeChance = 60;
    //TODO Convert into easier to manipulate units
    public static final int PlayerTopSpeed = 500;
    public static final float FirstFightMult = 1.5f;

    //TODO Smoother movement
    public static final int playerAcceleration = 50;

    public static final Point2 MinBodyPartSize = Screen.pos(7, 7);
    public static final Point2 MaxBodyPartSize = Screen.pos(13, 13);
    public static final int MinBodyParts = 6;
    public static final int MaxBodyParts = 10;

    public static final int MinMergeImpactPercent = 15;
    public static final int MaxMergeImpactPercent = 30;
    public static final int MergeRejectGrowthPercent = 40;

    public static final int BonusAmount = 1;
    public static final int BonusAward = 15;

    public static final int InitEnabledStats = 1;
    public static final int MinStatInit = 2;
    public static final int MaxStatInit = 5;
    public static final int DisableStat = 0;
    public static final int MinStat = 0;
    public static final int MaxStat = 100;

    public static final int SliceScale = 12;
    public static final int AbrasiveScale = 10;
    public static final int ExplosiveScale = 30;
    public static final int ContractionScale = 3;
    public static final int VaporizeScale = 15;
    public static final int ExpansionScale = 6;

    public static final int MinInitCooldownMilliSeconds = 500;
    public static final int MaxInitCooldownMilliSeconds = 800;
    public static final float MinCoolDown = .1f;

    public static final float MinScaleDeath = .5f;
    public static final float MaxScaleDeath = 1.5f;

    public static final float ForceColorIntensity = .85f;
    public static final float ForceColorMix = .4f;

    public static final int MaxForcesEnabled = 4;

    //Optimizations
    //TODO Make them toggleable in the options menu,
    //     helps with lots of creatures running on weaker computers
    public static final boolean OptCollectMetaData = true;
    public static final boolean OptDisableOutlines = false;
    public static final boolean OptDisableCloudyTextures = false;
    public static final boolean OptFastOutlineAlg = false;
    public static final boolean OptShowFPS = false;
    public static final boolean OptEnableFontOutlines = true;

    //Bots
    public static final boolean DevBotEnabled = false;
    public static final boolean DevBotCatchIO = false;
    public static final boolean DevBotAlwaysMerge = true;

    //Debugging / Development
    public static final boolean DevShortcutsEnabled = true;
    public static final boolean DevEndToEndStateLoadTest = false;
    public static final boolean DevFlipEnabled = false;
    public static final boolean DevPartSortingEnabled = false;
    public static final boolean DevDrawSkeleton = false;
    public static final boolean DevDebugJointGrid = false;
    public static final boolean DevDebugJointGridWithSquares = false;
    public static final boolean DevAlwaysSelectForces = false;
    public static final boolean DevDrainEnergyCommand = false;

    //Debug/Development Logging toggles
    public static final boolean DevBattleLog = false;
    public static final boolean DevTimeStates = false;
    public static final boolean DevPrintArenaSize = false;

    public static final int MeterOutlinePixelThickness = 2;
    public static final int BodyPartOutlinePixelThickness = 5;

    public static String json() {
        String config = "\"gameConfig\":{";
        Field[] fields = GameConfig.class.getDeclaredFields();
        int c = 0;
        for (Field f : fields) {
            try {
                config += "\"" + f.getName() + "\":\"" + f.get(null) + "\"";
                if (c++ < fields.length - 1) {
                    config += ",";
                }
            }
            catch (IllegalAccessException e) {
                Logger.exception(e);
            }
        }
        config += "}";
        return config;
    }
}
