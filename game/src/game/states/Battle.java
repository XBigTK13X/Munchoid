package game.states;

import game.GameConfig;
import game.Score;
import game.battle.EnergyMeter;
import game.battle.ForcesHUD;
import game.battle.HealthMeter;
import game.creatures.Creature;
import game.forces.Force;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.Commands;
import sps.core.Logger;
import sps.entities.EntityManager;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.ui.ToolTip;
import sps.util.Screen;

public class Battle implements State {
    private static SingleSongPlayer __battleMusic;
    private Creature _left;
    private Creature _right;
    private ForcesHUD _leftUI;
    private ForcesHUD _rightUI;
    private HealthMeter _leftHealth;
    private HealthMeter _rightHealth;
    private EnergyMeter _leftEnergy;
    private EnergyMeter _rightEnergy;
    private Text _leftCoolDown;
    private Text _rightCoolDown;
    private boolean _isFinalBattle;

    public Battle() {
        this(new Creature(), new Creature());
        Logger.error("ONLY USE Battle() FOR DEBUGGING!!!");
    }

    public Battle(Creature slot1, Creature slot2) {
        _left = slot1;
        _right = slot2;
    }

    public Battle(Creature slot1, Creature slot2, boolean isFinalBattle) {
        this(slot1, slot2);
        _isFinalBattle = isFinalBattle;
    }

    @Override
    public void create() {
        _left.orientX(false, true);
        _right.orientX(true, true);

        EntityManager.get().addEntity(_right);
        _right.getBody().setScale(1);
        _left.getBody().setScale(1);
        EntityManager.get().addEntity(_left);
        _right.setOpponent(_left);
        _left.setOpponent(_right);

        _leftUI = new ForcesHUD(_left);
        _rightUI = new ForcesHUD(_right);

        _leftHealth = new HealthMeter(_left);
        _rightHealth = new HealthMeter(_right);

        _leftEnergy = new EnergyMeter(_left);
        _rightEnergy = new EnergyMeter(_right);

        _leftCoolDown = TextPool.get().write(coolDownText(_left), Screen.pos(15, 15));
        _rightCoolDown = TextPool.get().write(coolDownText(_right), Screen.pos(65, 15));

        TextPool.get().write(_left.getName(), Screen.pos(0, 50).add((int) _left.getLocation().X, 0));
        TextPool.get().write(_right.getName(), Screen.pos(0, 50).add((int) _right.getLocation().X, 0));
    }

    private String coolDownText(Creature creature) {
        return String.format("%.2f", creature.getCoolDown().getTimeLeft()) + " sec";
    }

    @Override
    public void draw() {
        EntityManager.get().draw();
        //TODO makes these proper entities to enable depth sorting
        _leftUI.draw();
        _rightUI.draw();
        _leftHealth.draw();
        _rightHealth.draw();
        _leftEnergy.draw();
        _rightEnergy.draw();
    }

    public void playerAttack(Force force) {
        if (_left.getCoolDown().isCooled()) {
            _left.attack(force);
        }
    }

    @Override
    public void update() {
        EntityManager.get().update();
        _leftHealth.update();
        _rightHealth.update();
        _rightEnergy.update();
        _leftEnergy.update();


        _rightCoolDown.setMessage(coolDownText(_right));
        _leftCoolDown.setMessage(coolDownText(_left));
        _rightCoolDown.setVisible(_right.getCoolDown().getTimeLeft() != 0);
        _leftCoolDown.setVisible(_left.getCoolDown().getTimeLeft() != 0);
        if (_left.getCoolDown().isCooled()) {
            for (Force force : Force.values()) {
                if (Input.get().isActive(Commands.get(force.Command), 0) && _left.getStats().isEnabled(force)) {
                    if (_left.canUse(force)) {
                        if (_left.getStats().get(force) > 0) {
                            playerAttack(force);
                        }
                        else {
                            TextPool.get().write(force.name() + " Disabled", Screen.pos(10, 50), 1f, TextEffects.Fountain);
                        }
                    }
                    else {
                        TextPool.get().write("Not enough energy", Screen.pos(10, 50), 1f, TextEffects.Fountain);
                    }
                }
            }
            if (Input.get().isActive(Commands.get("Pop")) && GameConfig.DevShortcutsEnabled) {
                victory();
            }
        }
        else if (_right.getCoolDown().isCooled()) {
            //TODO Smarter attacks
            _right.attack(_right.getStats().randomEnabledForce());
        }
        else {
            _right.regenEnergy();
            _left.regenEnergy();
            _right.getCoolDown().update();
            _left.getCoolDown().update();
        }


        if (!_right.getBody().isAlive() || GameConfig.DevPlaythroughTest) {
            victory();
        }
        if (!_left.getBody().isAlive()) {
            Score.get().setPlayerPetStats(_left.getStats());
            StateManager.reset().push(new GameLose());
        }
    }

    private void victory() {
        Score.get().addVictory();
        Score.get().addHealthRemaining(_left.getBody().getPercentHealth());
        _left.getBody().restore();
        StateManager.get().pop();
        if (_isFinalBattle) {
            Score.get().setPlayerPetStats(_left.getStats());
            StateManager.get().push(new GameWin());
        }
        else {
            StateManager.get().push(new MergeOutcome(_left, _right));
        }
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        if (__battleMusic == null) {
            __battleMusic = new SingleSongPlayer("BattleTheme.ogg");
        }
        MusicPlayer.get(__battleMusic);
        MusicPlayer.get().start();
    }

    @Override
    public void unload() {
        EntityManager.get().clear();
        ToolTip.reset();
    }

    @Override
    public String getName() {
        return "Battle";
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }
}
