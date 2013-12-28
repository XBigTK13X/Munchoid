package game.population;

import game.core.GameConfig;
import sps.util.Maths;

public class Population {

    private int _size;

    public Population(int startingSize) {
        _size = startingSize;
    }

    public void grow() {
        _size += _size * (GameConfig.NaturalPopulationGrowthPercent / 100f);
        _size = Maths.clamp(_size, 0, GameConfig.PopulationMax);
    }

    public int getSize() {
        return _size;
    }

    public void setSize(int size) {
        _size = size;
    }

    public int deathsCausedBy(DeathCause deathCause) {
        return (int) (DeathCause.InfluenceMultiplier * deathCause.getDeathsPerInfluence() * (_size / DeathCause.PopulationInfluence));

    }
}
