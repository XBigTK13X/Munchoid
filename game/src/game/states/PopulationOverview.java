package game.states;

import game.GameConfig;
import game.population.Disease;
import game.population.Diseases;
import game.population.Population;
import sps.display.Screen;
import sps.states.State;
import sps.text.Text;
import sps.text.TextPool;

import java.util.List;

public class PopulationOverview implements State {

    private Population _population;
    private List<Disease> _top;
    private List<Disease> _bottom;

    private Text _topDisplay;
    private Text _bottomDisplay;

    @Override
    public void create() {
        _population = new Population();
        _top = Diseases.get().getRandomTop(GameConfig.NumberOfTournaments);
        _bottom = Diseases.get().getRandomBottom(GameConfig.NumberOfTournaments);


        String tD = "TOP\n";
        for (Disease d : _top) {
            tD += d.Name + "\n";
        }

        String bD = "BOTTOM\n";
        for (Disease d : _bottom) {
            bD += d.Name + "\n";
        }

        _topDisplay = TextPool.get().write(tD, Screen.pos(5, 95));
        _topDisplay.setScale(.5f);
        _bottomDisplay = TextPool.get().write(bD, Screen.pos(75, 95));
        _bottomDisplay.setScale(.5f);


    }

    public void addWin() {
        //TODO simulate pop changes and disease negation
    }

    public void addLoss() {

    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
    }

    @Override
    public String getName() {
        return "PopulationOverview";
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
