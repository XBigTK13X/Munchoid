package game.forceselection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.InputWrapper;
import game.arena.Arena;
import game.creatures.Creature;
import game.tournament.Tournament;
import game.ui.UISprite;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.DrawDepths;
import sps.display.Screen;
import sps.display.Window;
import sps.entities.EntityManager;
import sps.entities.HitTest;
import sps.states.State;
import sps.states.StateManager;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.Buttons;
import sps.ui.Tooltips;

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
        if (_pet.getStats().possibleActiveForces() <= 4 && !GameConfig.DevAlwaysSelectForces) {
            exitMenu();
            return;
        }
        TextPool.get().write("ENABLED", Screen.pos(20, 100));
        TextPool.get().write("DISABLED", Screen.pos(70, 100));

        if (_accept == null) {
            setMessage();
            _accept = UISprite.button(Color.GREEN);

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
        Tooltips.get().add(new Tooltips.User() {
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
        if (GameConfig.DevEndToEndStateLoadTest) {
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
        Window.get().render(_accept, DrawDepths.get("Default").DrawDepth);
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
        if (GameConfig.DevBotEnabled) {
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
