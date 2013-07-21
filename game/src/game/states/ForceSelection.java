package game.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.UI;
import game.creatures.Creature;
import game.forceselection.ForcesSelectionUI;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.Commands;
import sps.entities.EntityManager;
import sps.entities.HitTest;
import sps.graphics.Window;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.Buttons;
import sps.ui.ToolTip;
import sps.util.Screen;

public class ForceSelection implements State {
    private static SingleSongPlayer __mergeMusic;
    private Creature _pet;

    private Sprite _accept;
    private ForcesSelectionUI _forces;
    private Text _wrongCountMessage;

    public ForceSelection(Creature pet) {
        _pet = pet;
    }

    @Override
    public void create() {
        //TODO Option that automatically chooses
        if (_pet.getStats().possibleActiveForces() <= 4) {
            exitMenu();
            return;
        }
        TextPool.get().write("ENABLED", Screen.pos(20, 100));
        TextPool.get().write("DISABLED", Screen.pos(70, 100));

        if (_accept == null) {
            //TODO Change the wording to "ENABLE X" if not enough, and "DISABLE X" if too many
            setMessage();
            _accept = UI.button(Color.GREEN);

            _accept.setPosition(Screen.width(50), Screen.height(10));

            Buttons.get().add(new Buttons.User() {
                @Override
                public Sprite getSprite() {
                    return _accept;
                }

                @Override
                public void onClick() {
                    confirmSelection();
                }
            });
        }
        ToolTip.get().add(new ToolTip.User() {
            @Override
            public boolean isActive() {
                return HitTest.mouseInside(_accept);
            }

            @Override
            public String message() {
                return "Confirm";
            }
        });
        _forces = new ForcesSelectionUI(_pet);
        if (GameConfig.DevPlaythroughTest) {
            while (!confirmSelection()) {
                _pet.getStats().setEnabled(_pet.getStats().randomEnabledForce(), false);
            }
        }
    }

    private void exitMenu() {
        StateManager.get().pop();
        StateManager.get().pop();
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
        Window.get().draw(_accept);
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
        if (Input.get().isActive(Commands.get("Confirm"))) {
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
