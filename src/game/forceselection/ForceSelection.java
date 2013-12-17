package game.forceselection;

import com.badlogic.gdx.graphics.Color;
import game.DevConfig;
import game.InputWrapper;
import game.arena.Arena;
import game.creatures.Creature;
import game.tournament.Tournament;
import game.ui.UIButton;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.DrawDepths;
import sps.color.Colors;
import sps.display.Screen;
import sps.entities.EntityManager;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;

public class ForceSelection implements State {
    private static SingleSongPlayer __mergeMusic;
    private Creature _pet;

    private ForcesSelectionUI _forces;
    private Text _wrongCountMessage;

    public ForceSelection(Creature pet) {
        _pet = pet;
    }

    @Override
    public void create() {
        //TODO Option that automatically chooses
        if (_pet.getStats().possibleActiveForces() <= 4 && !DevConfig.AlwaysSelectForces) {
            exitMenu();
            return;
        }
        TextPool.get().write("ENABLED", Screen.pos(20, 100));
        TextPool.get().write("DISABLED", Screen.pos(70, 100));

        setMessage();

        final UIButton accept = new UIButton("Confirm") {
            @Override
            public void click() {
                confirmSelection();
            }
        };

        accept.setSize(10, 8);
        accept.setXY(50, 10);
        accept.setBackgroundColors(Colors.brightnessShift(Color.GREEN, -80), Colors.brightnessShift(Color.GREEN, -45));
        accept.setDepth(DrawDepths.get("ForceAccept"));

        _forces = new ForcesSelectionUI(_pet);
        if (DevConfig.EndToEndStateLoadTest) {
            while (!confirmSelection()) {
                _pet.getStats().setEnabled(_pet.getStats().randomEnabledForce(), false);
            }
        }
    }

    private void exitMenu() {
        StateManager.get().rollBackTo(StateManager.get().hasAny(Tournament.class) ? Tournament.class : Arena.class);
    }

    private boolean confirmSelection() {
        if (_pet.getStats().enabledCount() == _pet.getStats().maxEnabled()) {
            exitMenu();
            return true;
        }
        else {
            _wrongCountMessage.setVisible(true);
            return false;
        }
    }

    @Override
    public void draw() {
        _forces.draw();
    }

    private String getMessage() {
        int diff = _pet.getStats().enabledCount() - _pet.getStats().maxEnabled();
        if (diff > 0) {
            return "Please disable " + (_pet.getStats().enabledCount() - _pet.getStats().maxEnabled()) + " of the forces on the left.";
        }
        else if (diff == 0) {
            return "Please press the green button to accept your changes.";
        }
        else {
            return "Please enable " + (_pet.getStats().maxEnabled() - _pet.getStats().enabledCount()) + " of the forces on the right";
        }
    }

    private int _lastEnabledCount;

    private void setMessage() {
        if (_wrongCountMessage == null) {
            _wrongCountMessage = TextPool.get().write(getMessage(), Screen.pos(10, 30));
        }
        else {
            if (_lastEnabledCount != _pet.getStats().enabledCount()) {
                _lastEnabledCount = _pet.getStats().enabledCount();
                _wrongCountMessage.setMessage(getMessage());
            }
        }
    }

    @Override
    public void update() {
        setMessage();
        if (DevConfig.BotEnabled) {
            if (!confirmSelection()) {
                _pet.getStats().setEnabled(_pet.getStats().randomEnabledForce(), false);
            }
        }
        if (InputWrapper.confirm()) {
            confirmSelection();
        }
    }

    @Override
    public void asyncUpdate() {
    }

    @Override
    public void load() {
        if (__mergeMusic == null) {
            __mergeMusic = new SingleSongPlayer("MergeTheme.ogg");
        }
        MusicPlayer.get(__mergeMusic);
        MusicPlayer.get().start();
    }

    @Override
    public void unload() {
        EntityManager.get().clear();
        MusicPlayer.get().stop();
    }

    @Override
    public String getName() {
        return "ForceSelection";
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        MusicPlayer.get().stop();
    }
}
