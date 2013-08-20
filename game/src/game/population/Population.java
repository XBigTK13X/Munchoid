package game.population;

import game.GameConfig;
import sps.util.MathHelper;

public class Population {

    private int _size;

    public Population() {
        _size = GameConfig.StartingPopulationSize;
    }

    public void grow() {
        _size += _size * (GameConfig.NaturalPopulationGrowthPercent / 100f);
        _size = MathHelper.clamp(_size, 0, GameConfig.PopulationMax);
    }

    public int getSize() {
        return _size;
    }

    public void setSize(int size) {
        _size = size;
    }

    public int deathsCausedBy(Disease disease) {
        return (int) (Disease.InfluenceMultiplier * disease.DeathsPerInfluence * (_size / Disease.PopulationInfluence));

    }
}
