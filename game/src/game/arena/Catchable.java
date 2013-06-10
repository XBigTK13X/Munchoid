package game.arena;

import game.GameConfig;
import game.creatures.Creature;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.graphics.Renderer;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.util.Screen;

public class Catchable extends Entity {

    private static final int __moveIncrementsMax = 30;

    private Creature _creature;
    private int _moveIncrements = 0;
    private Point2 _movementTarget = new Point2();
    private int __pace = 10;
    private float _dX = 0;
    private float _dY = 0;
    private static Player __player;

    public Catchable(Player player) {
        if (__player == null) {
            __player = player;
        }
        initialize(0, 0, Point2.Zero, null, EntityTypes.get("Catchable"), DrawDepths.get("Catchable"));
        _creature = new Creature();
        _creature.getBody().setScale(GameConfig.ArenaCreatureScale);
        _creature.orientX((GameConfig.DevFlipEnabled) ? RNG.coinFlip() : false, false);
        setSize(_creature.getWidth(), _creature.getHeight());
        setLocation(Screen.rand(-GameConfig.ArenaBufferPercent, 100 + GameConfig.ArenaBufferPercent, -GameConfig.ArenaBufferPercent, 100 + GameConfig.ArenaBufferPercent));
    }

    @Override
    public void update() {
        if (!_creature.getBody().isAlive()) {
            setInactive();
        }

        _creature.setLocation(getLocation());

        if (__player.getNet() != null && __player.getNet().isInUse() && __player.getNet().isTouching(_creature)) {
            if (__player.getPet() == null) {
                __player.setPet(_creature);
                setInactive();
                MusicPlayer.get(new SingleSongPlayer("Quickly.ogg"));
                MusicPlayer.get().start();
            }
            else {
                if (__player.getPet().isLargerThan(_creature)) {
                    //TODO Chomping sound effect here
                    TextPool.get().write("*CHOMP*", __player.getLocation(), 1f, TextEffects.Fountain);
                    __player.getPet().addBonus(Math.min(__player.getPet().getStats().power() - _creature.getStats().power(), _creature.getStats().power()));
                    setInactive();
                }
                else {
                    __player.freeze();
                    _creature.addBonus(Math.min(_creature.getStats().power() - __player.getPet().getStats().power(), __player.getPet().getStats().power()));
                }
            }
        }

        if (_moveIncrements > 0) {
            _dX = (_movementTarget.X - getLocation().X) * __pace / __moveIncrementsMax;
            _dY = (_movementTarget.Y - getLocation().Y) * __pace / __moveIncrementsMax;
            _moveIncrements--;
        }
        else {
            _moveIncrements = RNG.next(__moveIncrementsMax / 2, __moveIncrementsMax);
            _movementTarget = getLocation().addRaw(Screen.rand(-10, 10, -10, 10));
        }

        if (GameConfig.DevFlipEnabled) {
            _creature.orientX(_dX <= 0, false);
        }

        //TODO Fix movement. Currently, edges snag the catchable.
        move(_dX, _dY);
    }

    @Override
    public void draw() {
        _creature.draw();
    }

    public Creature getCreature() {
        return _creature;
    }

    public void setCreature(Creature creature) {
        _creature = creature;
        setSize(_creature.getWidth(), _creature.getHeight());
        _creature.getBody().setScale(GameConfig.ArenaCreatureScale);
    }

    @Override
    public Point2 getLocation() {
        return _location.add(Renderer.get().getXOffset(), Renderer.get().getYOffset());
    }


}
