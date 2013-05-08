package game.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.creatures.Creature;
import game.creatures.Merge;
import game.creatures.style.Outline;
import game.forces.Force;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.Commands;
import sps.entities.EntityManager;
import sps.entities.HitTest;
import sps.graphics.Renderer;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.ui.Bounds;
import sps.ui.Buttons;
import sps.ui.ToolTip;
import sps.util.Colors;
import sps.util.Screen;
import sps.util.SpriteMaker;

public class MergeOutcome implements State {
    private static SingleSongPlayer __mergeMusic;
    private Creature _defeated;
    private Creature _pet;
    private Creature _merged;

    private Sprite _reject;
    private Sprite _accept;
    private Color _acceptHighlight;
    private Color _rejectHighlight;
    private Bounds _acceptBounds;
    private Bounds _rejectBounds;

    public MergeOutcome(Creature pet, Creature defeated) {
        _pet = pet;
        _defeated = defeated;
    }

    @Override
    public void create() {
        //Create a new merged creature
        _pet.getBody().restore();
        _defeated.getBody().restore();
        _merged = Merge.two(_pet, _defeated);

        //Stat merge display
        TextPool.get().write("Merge Outcome:", Screen.pos(15, 80));
        int forceRow = 2;
        for (Force force : Force.values()) {
            String resultText = _merged.getStats().get(force) == GameConfig.MaxStat ? "MAX" : _merged.getStats().get(force) + "";
            String forceChange = force.name() + ": " + _pet.getStats().get(force) + " -> " + resultText;
            TextPool.get().write(forceChange, Screen.pos(15, 80 - forceRow * 5));
            forceRow++;
        }

        //Body merge display
        _pet.setLocation(Screen.pos(10, 10));
        TextPool.get().write("+", Screen.pos(25, 15));
        _defeated.setLocation(Screen.pos(30, 10));
        TextPool.get().write("=", Screen.pos(45, 15));
        _merged.setLocation(Screen.pos(65, 15));

        _pet.getBody().setScale(.5f);
        _defeated.getBody().setScale(.5f);
        _merged.getBody().setScale(.5f);

        //Accept and reject buttons
        if (_accept == null) {
            _accept = button(Color.GREEN);
            _reject = button(Color.RED);

            _accept.setPosition(Screen.width(60), Screen.height(80));
            _reject.setPosition(Screen.width(60), Screen.height(60));
            _rejectBounds = new Bounds(_reject.getX(), _reject.getY(), (int) _reject.getWidth(), (int) _reject.getHeight());
            _acceptBounds = new Bounds(_accept.getX(), _accept.getY(), (int) _accept.getWidth(), (int) _accept.getHeight());
            Buttons.get().add(new Buttons.User() {

                @Override
                public Bounds getBounds() {
                    return _acceptBounds;
                }

                @Override
                public void normal() {
                    _acceptHighlight = Color.WHITE;
                }

                @Override
                public void over() {
                    _acceptHighlight = Color.YELLOW;
                }

                @Override
                public void onClick() {
                    _acceptHighlight = Color.BLUE;
                    acceptMerge();
                }
            });
            Buttons.get().add(new Buttons.User() {

                @Override
                public Bounds getBounds() {
                    return _rejectBounds;
                }

                @Override
                public void normal() {
                    _rejectHighlight = Color.WHITE;
                }

                @Override
                public void over() {
                    _rejectHighlight = Color.YELLOW;
                }

                @Override
                public void onClick() {
                    _rejectHighlight = Color.BLUE;
                    rejectMerge();
                }
            });
        }
        ToolTip.get().add(new ToolTip.User() {

            @Override
            public boolean isActive() {
                return HitTest.mouseInside(_acceptBounds);
            }

            @Override
            public String message() {
                return "Perform Merge";
            }
        });

        ToolTip.get().add(new ToolTip.User() {

            @Override
            public boolean isActive() {
                return HitTest.mouseInside(_rejectBounds);
            }

            @Override
            public String message() {
                return "Prevent Merge";
            }
        });
    }

    private Sprite button(Color color) {
        Color[][] acceptBase = Colors.genArr((int) Screen.width(10), (int) Screen.height(8), color);
        Outline.complimentary(acceptBase);
        return SpriteMaker.get().fromColors(acceptBase);
    }

    @Override
    public void draw() {
        _pet.draw();
        _defeated.draw();
        _merged.draw();
        Renderer.get().draw(_accept, _acceptHighlight);
        Renderer.get().draw(_reject, _rejectHighlight);
    }

    private void acceptMerge() {
        _defeated.getBody().kill();
        _pet.reset(_merged);
        StateManager.get().pop();
    }

    private void rejectMerge() {
        _defeated.getBody().kill();
        StateManager.get().pop();
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Confirm"))) {
            acceptMerge();
        }
        if (Input.get().isActive(Commands.get("Push"))) {
            rejectMerge();
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
        return "MergeOutcome";
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        MusicPlayer.get().stop();
    }
}
