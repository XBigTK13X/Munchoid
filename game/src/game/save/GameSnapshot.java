package game.save;

import game.Score;
import game.population.DiseaseMonitor;
import game.population.Population;
import game.population.PopulationHUD;

public class GameSnapshot {
    public static final int Version = 2;

    public Population Population;
    public PopulationHUD PopulationHud;
    public String RegionName;
    public DiseaseMonitor TopDiseases;
    public DiseaseMonitor BottomDiseases;
    public Score Score;
    public int RecordedVersion;
    public int TournamentsPlayed;
    public int TournamentWins;
}
