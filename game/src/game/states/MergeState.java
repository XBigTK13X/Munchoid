package game.states;

import game.creatures.Creature;
import game.creatures.Stats;
import game.forces.Force;
import sps.bridge.Commands;
import sps.core.RNG;
import sps.entities.EntityManager;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.util.Screen;

public class MergeState implements State {
    private Creature _defeated;
    private Creature _pet;

    public MergeState(Creature pet, Creature defeated) {
        _pet = pet;
        _defeated = defeated;
    }

    @Override
    public void create() {
        TextPool.get().write("Merge results:", Screen.pos(15, 80));
        Stats preMerge = _pet.getStats();
        Stats incoming = _defeated.getStats();
        Stats merged = new Stats();
        int forceRow = 2;
        for (Force force : Force.values()) {
            int average = (preMerge.get(force) + incoming.get(force)) / 2;
            int impact = (int) (average * (RNG.next(15, 40) / 100f));
            merged.set(force, preMerge.get(force) + impact);
            TextPool.get().write(force.name() + ": " + preMerge.get(force) + " -> " + merged.get(force), Screen.pos(15, 80 - forceRow * 5));
            forceRow++;
        }
        _pet.setStats(merged);

        //TODO Merge in size as well as stats
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

    }

    @Override
    public void unload() {
        EntityManager.get().clear();
    }

    @Override
    public String getName() {
        return "Merge";
    }

    @Override
    public void resize(int width, int height) {
    }
}
