package game.states;

import game.GameConfig;
import game.InputWrapper;
import game.population.Disease;
import game.population.Diseases;
import game.population.Population;
import sps.bridge.Commands;
import sps.display.Screen;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

import java.util.List;

public class PopulationOverview implements State {

    private Population _population;
    private List<Disease> _top;
    private List<Disease> _bottom;

    private Text _topDisplay;
    private Text _bottomDisplay;

    private int _wins;
    private int _losses;

    @Override
    public void create() {
        _population = new Population();
        _top = Diseases.get().getRandomTop(GameConfig.NumberOfTournaments);
        _bottom = Diseases.get().getRandomBottom(GameConfig.NumberOfTournaments);

        _topDisplay = TextPool.get().write("", Screen.pos(5, 95));
        _bottomDisplay = TextPool.get().write("", Screen.pos(75, 95));

        updateDiseaseDisplay();

        TextPool.get().write("Press [" + Commands.get("Confirm").key() + "] to enter the next tournament", Screen.pos(10, 10));
    }

    private String diseaseDisplay(Disease d) {
        return d.Name + (d.isActive() ? "" : "(D)") + "\n";
    }

    private void updateDiseaseDisplay() {
        String tD = "TOP\n";
        for (Disease d : _top) {
            tD += diseaseDisplay(d);
        }

        String bD = "BOTTOM\n";
        for (Disease d : _bottom) {
            bD += diseaseDisplay(d);
        }

        _topDisplay.setMessage(tD);
        _topDisplay.setScale(.5f);
        _bottomDisplay.setMessage(bD);
        _bottomDisplay.setScale(.5f);
    }

    private void disableTopDisease() {
        _top.get(_wins).setActive(false);
    }

    private void disableBottomDisease() {
        _bottom.get(_wins + _losses).setActive(false);
        updateDiseaseDisplay();
    }

    public void addWin() {
        disableTopDisease();
        disableBottomDisease();
        _wins++;
    }

    public void addLoss() {
        disableBottomDisease();
        _losses++;
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (InputWrapper.confirm()) {
            StateManager.get().push(new LoadArena());
        }
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
