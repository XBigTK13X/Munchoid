package game.save;

import game.core.Score;
import game.population.DeathCauseMonitor;
import game.population.Population;
import game.population.PopulationHUD;

public class GameSnapshot {
    public static final int Version = 3;

    public Population Population;
    public PopulationHUD PopulationHud;
    public String RegionName;
    public DeathCauseMonitor TopDeathCauses;
    public DeathCauseMonitor BottomDeathCauses;
    public Score Score;
    public int RecordedVersion;
    public int TournamentsPlayed;
    public int TournamentWins;
}
