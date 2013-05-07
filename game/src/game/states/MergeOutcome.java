package game.states;

import game.GameConfig;
import game.creatures.Creature;
import game.creatures.Merge;
import game.creatures.Stats;
import game.forces.Force;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.Commands;
import sps.core.RNG;
import sps.entities.EntityManager;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.util.Screen;

public class MergeOutcome implements State {
    private static SingleSongPlayer __mergeMusic;
    private Creature _defeated;
    private Creature _pet;
    private Creature _merged;

    public MergeOutcome(Creature pet, Creature defeated) {
        _pet = pet;
        _defeated = defeated;
    }

    @Override
    public void create() {
        //Merge the stats
        TextPool.get().write("Merge Outcome:", Screen.pos(15, 80));
        Stats preMerge = _pet.getStats();
        Stats incoming = _defeated.getStats();
        Stats mergedStats = new Stats();
        int forceRow = 2;
        for (Force force : Force.values()) {
            int average = (preMerge.get(force) + incoming.get(force)) / 2;
            int impact = (int) (average * (RNG.next(GameConfig.MinMergeImpactPercent, GameConfig.MaxMergeImpactPercent) / 100f));
            if (impact == 0 && incoming.get(force) > 0) {
                impact = 1;
            }

            mergedStats.set(force, preMerge.get(force) + impact);
            String resultText = mergedStats.get(force) == GameConfig.MaxStat ? "MAX" : mergedStats.get(force) + "";
            String forceChange = force.name() + ": " + preMerge.get(force) + " -> " + resultText;
            TextPool.get().write(forceChange, Screen.pos(15, 80 - forceRow * 5));
            forceRow++;
        }


        //Merge the bodies
        _pet.getBody().restore();
        _pet.setLocation(Screen.pos(10, 10));
        _pet.getBody().setScale(.5f);
        TextPool.get().write("+", Screen.pos(25, 15));
        _defeated.getBody().restore();
        _defeated.setLocation(Screen.pos(30, 10));
        _defeated.getBody().setScale(.5f);
        TextPool.get().write("=", Screen.pos(45, 15));


        //Create a new merged creature
        _merged = Merge.two(_pet, _defeated);
        _merged.setStats(mergedStats);
        _merged.setLocation(Screen.pos(65, 15));


        //TODO MergeOutcome in size as well as stats/body
    }

    @Override
    public void draw() {
        _pet.draw();
        _defeated.draw();
        _merged.draw();
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Confirm"), 0)) {
            _defeated.getBody().kill();
            StateManager.get().pop();
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
