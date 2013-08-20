package game.population;

public class Disease implements Comparable {
    public static final int PopulationInfluence = 100_000;
    //TODO this might need to increase as the population growths larger
    public static final int InfluenceMultiplier = 63;

    public final float PercentOfDeaths;
    public final float DeathsPerInfluence;
    public final String Name;
    private boolean _active;

    public Disease(String name, float percentOfDeaths, float deathsPerInfluence) {
        _active = true;
        PercentOfDeaths = percentOfDeaths;
        Name = name;
        DeathsPerInfluence = deathsPerInfluence;
    }

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        _active = active;
    }

    @Override
    public int compareTo(Object o) {
        return (int) DeathsPerInfluence - (int) ((Disease) o).DeathsPerInfluence;
    }
}
