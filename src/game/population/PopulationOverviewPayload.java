package game.population;

public class PopulationOverviewPayload {
    private Population _population;
    private PopulationHUD _populationHud;
    private DeathCauseMonitor _top;
    private DeathCauseMonitor _bottom;

    public void cache(Population population) {
        _population = population;
    }


    public Population getPopulation() {
        return _population;
    }

    public void cache(PopulationHUD populationHud) {
        _populationHud = populationHud;
    }

    public PopulationHUD getPopulationHud() {
        return _populationHud;
    }

    public void cache(DeathCauseMonitor monitor, boolean top) {
        if (top) {
            _top = monitor;
        }
        else {
            _bottom = monitor;
        }
    }

    public DeathCauseMonitor getTop() {
        return _top;
    }

    public DeathCauseMonitor getBottom() {
        return _bottom;
    }
}