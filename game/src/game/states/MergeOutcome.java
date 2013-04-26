package game.states;

import game.GameConfig;
import game.creatures.Creature;
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

    public MergeOutcome(Creature pet, Creature defeated) {
        _pet = pet;
        _defeated = defeated;
    }

    @Override
    public void create() {
        TextPool.get().write("Merge Outcome:", Screen.pos(15, 80));
        Stats preMerge = _pet.getStats();
        Stats incoming = _defeated.getStats();
        Stats merged = new Stats();
        int forceRow = 2;
        for (Force force : Force.values()) {
            int average = (preMerge.get(force) + incoming.get(force)) / 2;
            int impact = (int) (average * (RNG.next(GameConfig.MinMergeImpactPercent, GameConfig.MaxMergeImpactPercent) / 100f));
            if (impact == 0 && incoming.get(force) > 0) {
                impact = 1;
            }

            merged.set(force, preMerge.get(force) + impact);
            String resultText = merged.get(force) == GameConfig.MaxStat ? "MAX" : merged.get(force) + "";
            TextPool.get().write(force.name() + ": " + preMerge.get(force) + " -> " + resultText, Screen.pos(15, 80 - forceRow * 5));
            forceRow++;
        }
        _pet.setStats(merged);

        //TODO MergeOutcome in size as well as stats
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Confirm"), 0)) {
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
