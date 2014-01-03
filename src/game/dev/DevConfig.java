package game.dev;

import sps.core.Loader;

public class DevConfig {
    private static Boolean __canEnable;

    private static boolean canEnable() {
        if (__canEnable == null) {
            __canEnable = !Loader.get().data("release_build").exists();
        }
        return __canEnable;
    }

    //Bots
    public static boolean BotEnabled = false;
    public static final boolean BotAlwaysMerge = false;
    public static final boolean BotRandomlyMerges = true;
    public static final boolean BotsLowQualityGraphics = true;

    //Debugging / Development
    public static final boolean ForceShortcutsEnabled = true && canEnable();
    public static final boolean EndGameTest = false && canEnable();
    public static final boolean SilhouetteTest = false && canEnable();
    public static final boolean TournyTest = false && canEnable();
    public static final boolean MergeOutcomeTest = false && canEnable();
    public static final boolean PopulationTest = false && canEnable();
    public static final boolean BattleTest = false && canEnable();
    public static final boolean ForceSelectionTest = false && canEnable();
    public static final boolean SkeletonTest = false && canEnable();
    public static final boolean BackgroundGenerationTest = false && canEnable();
    public static final boolean OutlineTest = false && canEnable();
    public static final boolean MeterTest = false && canEnable();
    public static final boolean ShortcutsEnabled = SilhouetteTest || EndGameTest || MeterTest || OutlineTest || ForceShortcutsEnabled || TournyTest || MergeOutcomeTest || PopulationTest || BattleTest || SkeletonTest || BackgroundGenerationTest;

    public static final boolean TestGameFreeze = false && canEnable();

    public static final boolean EndToEndStateLoadTest = false && canEnable();
    public static final boolean FlipEnabled = false && canEnable();
    public static final boolean PartSortingEnabled = false;
    public static final boolean DrawSkeleton = false && canEnable();
    public static final boolean DebugJointGrid = false && canEnable();
    public static final boolean DebugJointGridWithSquares = false && canEnable();
    public static final boolean AlwaysSelectForces = false && canEnable();
    public static final boolean UseOldCatchableMergeAlgorithm = false && canEnable();

    //Debug/Development Logging toggles
    public static final boolean BattleLog = false && canEnable();
    public static final boolean TimeStates = false && canEnable();
    public static final boolean PrintArenaSize = false && canEnable();
    public static final boolean DebugPopulationGrowth = false && canEnable();
}
