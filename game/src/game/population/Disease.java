package game.population;

public class Disease implements Comparable {
    public final float MortalityRate;
    public final float DeathsPer100000;
    public final String Name;
    private boolean _active;

    public Disease(String name, float mortalityRate, float deathsPer100000) {
        _active = true;
        MortalityRate = mortalityRate;
        Name = name;
        DeathsPer100000 = deathsPer100000;
    }

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        _active = active;
    }

    @Override
    public int compareTo(Object o) {
        return (int) DeathsPer100000 - (int) ((Disease) o).DeathsPer100000;
    }
}
