package game.population;

import game.GameConfig;

public class Population {

    private int _size;
    private int _hope;

    public Population() {
        _size = GameConfig.StartingPopulationSize;
        _hope = GameConfig.StartingPopulationHopePercent;
    }

    public void grow() {
        _size += _size * (GameConfig.NaturalPopulationGrowthPercent / 100f) * (_hope / 100f);
    }

    public void applyDisease(Disease disease) {
        _hope -= disease.MortalityRate;
    }
}
