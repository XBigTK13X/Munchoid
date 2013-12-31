package game.core;

public class WorldScore {
    public static int TournamentWins;
    public static int TournamentLosses;
    public static int ArenaTotal;

    public static void reset(int wins, int losses, int arenaTotal) {
        TournamentWins = wins;
        TournamentLosses = losses;
        ArenaTotal = arenaTotal;
    }
}
