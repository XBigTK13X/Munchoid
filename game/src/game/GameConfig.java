package game;

import sps.core.Point2;
import sps.util.Screen;

public class GameConfig {
    //The upper limit is ~400 with 2GB of memory.
    public final static int CreatureLimit = 50;

    public static final float ArenaCreatureScale = .3f;
    public final static float ArenaTimeoutSeconds = 2f;
    public final static int TournamentMatches = 3;

    public static final Point2 MinBodyPartDimensions = Screen.pos(5, 5);
    public static final Point2 MaxBodyPartDimensions = Screen.pos(10, 10);
}
