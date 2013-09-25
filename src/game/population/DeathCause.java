package game.population;

public class DeathCause implements Comparable {
    public static final int PopulationInfluence = 100_000;
    //TODO this might need to increase as the population growths larger
    public static final int InfluenceMultiplier = 63;

    private float _deathsPerInfluence;
    private String _name;

    private boolean _active;

    public DeathCause(String name, float deathsPerInfluence) {
        _active = true;
        _name = name;
        _deathsPerInfluence = deathsPerInfluence;
    }

    public String getName() {
        return _name;
    }

    public float getDeathsPerInfluence() {
        return _deathsPerInfluence;
    }

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        _active = active;
    }

    @Override
    public int compareTo(Object o) {
        return (int) _deathsPerInfluence - (int) ((DeathCause) o)._deathsPerInfluence;
    }
}
