package game.stages.population;

public class PopulationOverviewPayload {
    private Population _population;
    private PopulationHUD _populationHud;
    private DeathCauseMonitor _top;
    private DeathCauseMonitor _bottom;
    private String _regionName;
    private int _tournamentsPlayed;
    private int _tournamentWins;

    public void setPopulation(Population population) {
        _population = population;
    }


    public Population getPopulation() {
        return _population;
    }

    public void setPopulationHUD(PopulationHUD populationHud) {
        _populationHud = populationHud;
    }

    public PopulationHUD getPopulationHud() {
        return _populationHud;
    }

    public void setTopCauseOfDeathMonitor(DeathCauseMonitor monitor) {
        _top = monitor;
    }

    public void setBottomCauseOfDeathMonitor(DeathCauseMonitor monitor) {
        _bottom = monitor;
    }

    public DeathCauseMonitor getTop() {
        return _top;
    }

    public DeathCauseMonitor getBottom() {
        return _bottom;
    }

    public void setRegionName(String name) {
        _regionName = name;
    }

    public String getRegionName() {
        return _regionName;
    }

    public void setTournamentStats(int tournamentsPlayed, int tournamentWins) {
        _tournamentsPlayed = tournamentsPlayed;
        _tournamentWins = tournamentWins;
    }

    public int getTournamentsPlayed() {
        return _tournamentsPlayed;
    }

    public int getTournamentWins() {
        return _tournamentWins;
    }
}