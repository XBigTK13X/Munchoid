package game.states;

import game.arena.Player;
import game.creatures.Creature;
import sps.bridge.Commands;
import sps.core.Point2;
import sps.io.Input;
import sps.states.State;
import sps.states.StateManager;
import sps.text.TextPool;
import sps.util.Screen;

public class PreGame implements State {
    @Override
    public void create() {
        TextPool.get().write("Press SPACE to enter the arena.", Screen.pos(10, 60));
    }

    @Override
    public void draw() {
    }

    @Override
    public void update() {
        if (Input.get().isActive(Commands.get("Confirm"))) {
            Player player = new Player();
            player.setPet(new Creature(true, new Point2(150, 150), new Point2(170, 170)));
            StateManager.get().push(new Arena());
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
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void resize(int width, int height) {
    }
}
