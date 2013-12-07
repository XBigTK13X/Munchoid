package game;

public class DevConfig {
    //Bots
    public static boolean BotEnabled = false;
    public static final boolean BotAlwaysMerge = false;
    public static final boolean BotRandomlyMerges = true;
    public static final boolean BotsLowQualityGraphics = true;

    //Debugging / Development
    public static final boolean ForceShortcutsEnabled = true;
    public static final boolean TournyTest = false;
    public static final boolean MergeOutcomeTest = false;
    public static final boolean PopulationTest = false;
    public static final boolean BattleTest = false;
    public static final boolean SkeletonTest = false;
    public static final boolean BackgroundGenerationTest = false;
    public static final boolean ShortcutsEnabled = ForceShortcutsEnabled || TournyTest || MergeOutcomeTest || PopulationTest || BattleTest || SkeletonTest || BackgroundGenerationTest;

    public static final boolean EndToEndStateLoadTest = false;
    public static final boolean FlipEnabled = false;
    public static final boolean PartSortingEnabled = false;
    public static final boolean DrawSkeleton = false;
    public static final boolean DebugJointGrid = false;
    public static final boolean DebugJointGridWithSquares = false;
    public static final boolean AlwaysSelectForces = false;
    public static final boolean UseOldCatchableMergeAlgorithm = false;

    //Debug/Development Logging toggles
    public static final boolean BattleLog = false;
    public static final boolean TimeStates = false;
    public static final boolean PrintArenaSize = false;
    public static final boolean DebugPopulationGrowth = false;
}
