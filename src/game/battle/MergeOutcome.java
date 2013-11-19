package game.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.GameConfig;
import game.InputWrapper;
import game.Score;
import game.creatures.Creature;
import game.creatures.Merge;
import game.creatures.Stats;
import game.forces.Force;
import game.forceselection.ForceSelection;
import game.ui.UIButton;
import game.ui.UISprite;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.core.RNG;
import sps.display.Screen;
import sps.display.Window;
import sps.entities.EntityManager;
import sps.entities.HitTest;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.ui.Buttons;
import sps.ui.Tooltips;

public class MergeOutcome implements State {
    private static SingleSongPlayer __mergeMusic;
    private Creature _defeated;
    private Creature _pet;
    private Creature _merged;

    private Sprite _reject;
    private Sprite _accept;

    private UIButton _reroll;

    public MergeOutcome(Creature pet, Creature defeated) {
        _pet = pet;
        _defeated = defeated;
    }

    @Override
    public void create() {
        //Create a new merged creature
        _pet.restore();
        _defeated.restore();
        _pet.getBody().flipX(false);
        _defeated.getBody().flipX(false);
        _merged = Merge.creatures(_pet, _defeated);

        Stats cancelStats = Merge.rejectStats(_pet.getStats());

        final int left = 10;
        final int top = 90;

        //Stat merge display
        TextPool.get().write("Devour Outcome:", Screen.pos(left, top + 5));
        int forceRow = 2;
        for (Force force : Force.values()) {
            String resultText = cancelStats.get(force) == GameConfig.MaxStat ? "MAX" : cancelStats.get(force) + "";
            String forceChange = force.name() + ": " + _pet.getStats().get(force) + " -> " + resultText;
            TextPool.get().write(forceChange, Screen.pos(left, top - forceRow * 5));
            forceRow++;
        }

        TextPool.get().write("Merge Outcome:", Screen.pos(left + 50, top + 5));
        forceRow = 2;
        for (Force force : Force.values()) {
            String resultText = _merged.getStats().get(force) == GameConfig.MaxStat ? "MAX" : _merged.getStats().get(force) + "";
            String forceChange = force.name() + ": " + _pet.getStats().get(force) + " -> " + resultText;
            TextPool.get().write(forceChange, Screen.pos(left + 50, top - forceRow * 5));
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
            _accept = UISprite.button(Color.GREEN, 40, 50);
            _reject = UISprite.button(Color.RED, 40, 50);

            _reject.setPosition(Screen.width(5), Screen.height(35));
            _accept.setPosition(Screen.width(55), Screen.height(35));


            Buttons.get().add(new Buttons.User() {

                @Override
                public Sprite getSprite() {
                    return _accept;
                }

                @Override
                public void onClick() {
                    acceptMerge();
                }
            });
            Buttons.get().add(new Buttons.User() {

                @Override
                public Sprite getSprite() {
                    return _reject;
                }

                @Override
                public void onClick() {
                    rejectMerge();
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
                return "Click here or press " + Commands.get("Confirm") + "\nto merge with the opponent.";
            }
        });

        Tooltips.get().add(new Tooltips.User() {

            @Override
            public boolean isActive() {
                return HitTest.mouseInside(_reject);
            }

            @Override
            public String message() {
                return "Click here or press " + Commands.get("Push") + "\nto devour the opponent";
            }
        });
        _reroll = new UIButton("Start", Commands.get("Confirm")) {
            @Override
            public void click() {
                //TODO Reroll
            }
        };

        if (GameConfig.DevEndToEndStateLoadTest) {
            acceptMerge();
        }
        _pet.setStats(cancelStats);
    }

    @Override
    public void draw() {
        Window.get().schedule(_accept, DrawDepths.get("MergeChoice"));
        Window.get().schedule(_reject, DrawDepths.get("MergeChoice"));
        _pet.draw();
        _defeated.draw();
        _merged.draw();
        _reroll.draw();
    }

    private void acceptMerge() {
        Score.get().addMergeAccept();
        _defeated.getBody().kill();
        _pet.reset(_merged);
        loadNextScene();
    }

    private void rejectMerge() {
        Score.get().addMergeReject();
        _defeated.getBody().kill();
        loadNextScene();
    }

    private void loadNextScene() {
        StateManager.get().push(new ForceSelection(_pet));
    }

    @Override
    public void update() {
        if (InputWrapper.confirm()) {
            acceptMerge();
        }
        if (InputWrapper.push()) {
            rejectMerge();
        }
        if (GameConfig.DevBotEnabled) {
            if (GameConfig.DevBotRandomlyMerges) {
                if (RNG.coinFlip()) {
                    acceptMerge();
                }
                else {
                    rejectMerge();
                }
                return;
            }
            else {
                if (GameConfig.DevBotAlwaysMerge) {
                    acceptMerge();
                }
                else {
                    rejectMerge();
                }
            }
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
