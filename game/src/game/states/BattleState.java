package game.states;

import game.Shared;
import game.creatures.Creature;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.core.Point2;
import sps.entities.EntityManager;
import sps.states.State;
import sps.util.Screen;

public class BattleState implements State {
    private static SingleSongPlayer __battleMusic;

    private static final Point2 __creatureMinDimension = Screen.pos(15, 15);
    private static final Point2 __creatureMaxDimension = Screen.pos(40, 40);

    private static final Point2 __petLocation = Screen.pos(15, 15);

    @Override
    public void create() {
        EntityManager.get().addEntity(new Creature(true, __creatureMinDimension, __creatureMaxDimension));
        Shared.get().playerCreature().setLocation(__petLocation);
        EntityManager.get().addEntity(Shared.get().playerCreature());
        if (__battleMusic == null) {
            __battleMusic = new SingleSongPlayer("BattleTheme.ogg");
        }
        MusicPlayer.get(__battleMusic);
        MusicPlayer.get().start();
    }

    @Override
    public void draw() {
        EntityManager.get().draw();
    }

    @Override
    public void update() {
        EntityManager.get().update();
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
        MusicPlayer.get().stop();
    }

    @Override
    public String getName() {
        return "Battle";
    }

    @Override
    public void resize(int width, int height) {
    }
}
