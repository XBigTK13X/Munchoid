package game;

import sps.core.Point2;
import sps.util.Screen;

public class GameConfig {
    //The upper limit is ~400 with 2GB of memory.
    public final static int CreatureLimit = 10;

    public final static float ArenaTimeoutSeconds = 7f;
    public final static int TournamentMatches = 2;

    public static final Point2 MinBodyPartDimensions = Screen.pos(15, 15);
    public static final Point2 MaxBodyPartDimensions = Screen.pos(40, 40);
}
