package game.stages.battle;

import game.app.core.ArenaScore;
import config.GameConfig;
import game.app.core.InputWrapper;
import game.stages.common.creatures.Creature;
import game.stages.common.creatures.Merge;
import game.stages.common.creatures.Stats;
import game.app.dev.DevConfig;
import game.stages.common.forces.Force;
import game.stages.forceselection.ForceSelection;
import game.app.tutorial.Tutorials;
import sps.audio.MusicPlayer;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.color.Colors;
import sps.core.Logger;
import sps.core.RNG;
import sps.display.Screen;
import sps.entities.EntityManager;
import sps.entities.HitTest;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.ui.Tooltips;
import sps.ui.UIButton;

public class MergeOutcome implements State {
    private Creature _defeated;
    private Creature _pet;
    private Creature _merged;

    private UIButton _reroll;

    public MergeOutcome() {
        this(new Creature(), new Creature());
        Logger.info("ONLY USE MergeOutcome() for testing!");
    }

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

        final UIButton reject = new UIButton("") {
            @Override
            public void click() {
                rejectMerge();
            }
        };
        reject.setSize(40, 50);
        reject.setBackgroundColors(Colors.brightnessShift(Color.RED, -80), Colors.brightnessShift(Color.RED, -45));
        reject.setDepth(DrawDepths.get("MergeChoice"));
        reject.setXY((int) Screen.width(5), (int) Screen.height(35));

        final UIButton accept = new UIButton("") {
            @Override
            public void click() {
                acceptMerge();
            }
        };

        accept.setSize(40, 50);
        accept.setBackgroundColors(Colors.brightnessShift(Color.GREEN, -80), Colors.brightnessShift(Color.GREEN, -45));
        accept.setDepth(DrawDepths.get("MergeChoice"));
        accept.setXY((int) Screen.width(55), (int) Screen.height(35));

        accept.layout();
        reject.layout();

        Tooltips.get().add(new Tooltips.User() {

            @Override
            public boolean isActive() {
                return HitTest.mouseInside(accept.getSprite());
            }

            @Override
            public String message() {
                return "Click here or press " + Commands.get("Confirm") + "\nto merge with the opponent.";
            }
        });

        Tooltips.get().add(new Tooltips.User() {

            @Override
            public boolean isActive() {
                return HitTest.mouseInside(reject.getSprite());
            }

            @Override
            public String message() {
                return "Click here or press " + Commands.get("Push") + "\nto devour the opponent";
            }
        });

        //Body merge display
        _reroll = new UIButton("Reroll") {
            @Override
            public void click() {
                _merged.setBody(Merge.body(_pet, _defeated));
                _merged.getBody().setScale(.5f);
            }
        };

        int yB = 15;
        int yB5 = yB + 5;
        _pet.setLocation(Screen.pos(10, yB));
        TextPool.get().write("+", Screen.pos(25, yB5 + _pet.getHeight() / 2));
        _defeated.setLocation(Screen.pos(30, yB));
        TextPool.get().write("=", Screen.pos(45, yB5 + _pet.getHeight() / 2));
        _merged.setLocation(Screen.pos(50, yB));
        _reroll.setScreenPercent(70, yB - 5);

        _reroll.layout();

        _pet.getBody().setScale(.5f);
        _defeated.getBody().setScale(.5f);
        _merged.getBody().setScale(.5f);


        if (DevConfig.EndToEndStateLoadTest) {
            acceptMerge();
        }
        _pet.setStats(cancelStats);

        Tutorials.get().show();
    }

    @Override
    public void draw() {
        _pet.draw();
        _defeated.draw();
        _merged.draw();
        _reroll.draw();
    }

    private void acceptMerge() {
        ArenaScore.get().addMergeAccept();
        _defeated.getBody().kill();
        _pet.reset(_merged);
        loadNextScene();
    }

    private void rejectMerge() {
        ArenaScore.get().addMergeReject();
        _defeated.getBody().kill();
        loadNextScene();
    }

    private void loadNextScene() {
        if (DevConfig.MergeOutcomeTest) {
            StateManager.get().push(new MergeOutcome());
        }
        else {
            StateManager.get().push(new ForceSelection(_pet));
        }
    }

    @Override
    public void update() {
        if (InputWrapper.confirm()) {
            acceptMerge();
        }
        if (InputWrapper.push()) {
            rejectMerge();
        }
        if (DevConfig.BotEnabled) {
            if (DevConfig.BotRandomlyMerges) {
                if (RNG.coinFlip()) {
                    acceptMerge();
                }
                else {
                    rejectMerge();
                }
                return;
            }
            else {
                if (DevConfig.BotAlwaysMerge) {
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

        MusicPlayer.get().play("MergeTheme");
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
    public void pause() {
        MusicPlayer.get().stop();
    }
}
