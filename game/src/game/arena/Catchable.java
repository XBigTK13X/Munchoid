package game.arena;

import game.GameConfig;
import game.creatures.Creature;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.util.Screen;

public class Catchable extends Entity {
    private Creature _creature;

    private static final int __moveIncrementsMax = 30;
    private int _moveIncrements = 0;
    private int __pace = 10;

    private float _dX = 0;
    private float _dY = 0;

    private Point2 _movementTarget = new Point2();

    public Catchable() {
        initialize(0, 0, Point2.Zero, null, EntityTypes.get("Catchable"), DrawDepths.get("Catchable"));
        _creature = new Creature();
        _creature.getBody().setScale(GameConfig.ArenaCreatureScale);

        setSize(_creature.getWidth(), _creature.getHeight());
        setLocation(Screen.rand(15, 85, 15, 85));
    }

    @Override
    public void update() {
        if (!_creature.getBody().isAlive()) {
            setInactive();
        }

        _creature.setLocation(getLocation());

        CatchNet net = (CatchNet) EntityManager.get().getEntity(EntityTypes.get("Hand"));
        Player player = (Player) EntityManager.get().getPlayers().get(0);

        if (player.getPet() != null) {
            if (player.getPet().getStats().power() >= _creature.getStats().power()) {
                //TODO Indicate that it can be eaten
                //Flashing is built into _graphic, can we write the arrays to the sprite?
            }
        }

        if (net != null && net.isInUse() && net.isTouching(_creature)) {
            if (player.getPet() == null) {
                player.setPet(_creature);
                setInactive();
            }
            else {
                if (player.getPet().isLargerThan(_creature)) {
                    //TODO Chomping sound effect here
                    TextPool.get().write("*CHOMP*", player.getLocation(), 1f, TextEffects.Fountain);
                    player.getPet().addBonus(player.getPet().getStats().power() - _creature.getStats().power());
                    setInactive();
                }
                else {
                    _creature.addBonus(_creature.getStats().power() - player.getPet().getStats().power());
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
            _movementTarget = Screen.rand(5, 95, 5, 95);
        }

        _creature.orientX(_dX <= 0, false);

        //TODO Fix movement. Currently, edges snag the catchable.
        move(_dX, _dY);
    }

    @Override
    public void draw() {
        _creature.draw();
    }

    public Creature getPet() {
        return _creature;
    }
}
