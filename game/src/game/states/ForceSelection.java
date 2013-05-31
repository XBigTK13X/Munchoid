package game.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.UI;
import game.battle.ForcesHUD;
import game.creatures.Creature;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.Commands;
import sps.entities.EntityManager;
import sps.entities.HitTest;
import sps.graphics.Renderer;
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
    private ForcesHUD _forces;
    private Text _wrongCountMessage;

    public ForceSelection(Creature pet) {
        _pet = pet;
    }

    @Override
    public void create() {
        TextPool.get().write("ENABLED", Screen.pos(20, 100));
        TextPool.get().write("DISABLED", Screen.pos(70, 100));

        if (_accept == null) {
            //TODO Change the wording to "ENABLE X" if not enough, and "DISABLE X" if too many
            _wrongCountMessage = TextPool.get().write("Exactly " + _pet.getStats().maxEnabled() + " Forces Must Be Enabled", Screen.pos(10, 30));
            _wrongCountMessage.hide();
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
        _forces = new ForcesHUD(_pet);
        if (GameConfig.PlaythroughTest) {
            while (!confirmSelection()) {
                _pet.getStats().setEnabled(_pet.getStats().randomEnabledForce(), false);
            }
        }
    }

    private boolean confirmSelection() {
        if (_pet.getStats().enabledCount() == _pet.getStats().maxEnabled()) {
            StateManager.get().pop();
            StateManager.get().pop();
            return true;
        }
        else {
            _wrongCountMessage.setVisible(true);
            return false;
        }
    }

    @Override
    public void draw() {
        Renderer.get().draw(_accept);
        _forces.draw();
    }

    @Override
    public void update() {
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
