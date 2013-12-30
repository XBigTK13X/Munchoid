package game.population;

import game.core.GameConfig;
import sps.core.RNG;
import sps.util.Maths;

public class Population {

    private int _size;

    public Population(int startingSize) {
        setSize(startingSize);
    }

    public int getSize() {
        return _size;
    }

    public void setSize(int size) {
        _size = Maths.clamp(size, 0, GameConfig.PopulationMax);
    }

    public int getGrowth() {
        return (int) (_size * (GameConfig.NaturalPopulationGrowthPercent / 100f)) + RNG.next(1000);
    }

    public int deathsCausedBy(DeathCause deathCause) {
        return (int) (DeathCause.InfluenceMultiplier * deathCause.getDeathsPerInfluence() * (_size / DeathCause.PopulationInfluence));

    }
}
