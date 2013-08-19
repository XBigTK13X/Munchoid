package game.states;

import game.GameConfig;
import game.InputWrapper;
import game.population.Disease;
import game.population.Diseases;
import game.population.Population;
import game.population.PopulationHUD;
import sps.bridge.Commands;
import sps.core.Point2;
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

    private PopulationHUD _populationHud;
    private Text _populationCountDisplay;

    private int _wins;
    private int _losses;

    @Override
    public void create() {
        _population = new Population();
        Point2 hudSize = Screen.pos(40, 70);
        Point2 hudPosition = Screen.pos(30, 15);
        _populationHud = new PopulationHUD(_population, hudSize, hudPosition);
        _top = Diseases.get().getRandomTop(GameConfig.NumberOfTournaments);
        _bottom = Diseases.get().getRandomBottom(GameConfig.NumberOfTournaments);

        _topDisplay = TextPool.get().write("", Screen.pos(5, 95));
        _bottomDisplay = TextPool.get().write("", Screen.pos(75, 95));

        _populationCountDisplay = TextPool.get().write("", Screen.pos(30, 90));

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

        _populationHud.recalcIcons();

        _populationCountDisplay.setMessage("Population Size: " + _population.getSize() + " people");
    }

    private void disableTopDisease() {
        _top.get(_wins).setActive(false);
    }

    private void disableBottomDisease() {
        _bottom.get(_wins + _losses).setActive(false);
        simluatePopulationChange();
    }

    private void simluatePopulationChange() {
        _population.grow();
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
        _populationHud.draw();
    }

    @Override
    public void update() {
        if (InputWrapper.confirm()) {
            StateManager.get().push(new LoadArena());
        }
        if (GameConfig.DevPopulationTest) {
            if (InputWrapper.pop()) {
                simluatePopulationChange();
            }
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
