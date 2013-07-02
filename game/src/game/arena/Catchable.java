package game.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import game.GameConfig;
import game.Score;
import game.creatures.Creature;
import game.states.Arena;
import sps.audio.MusicPlayer;
import sps.audio.SingleSongPlayer;
import sps.bridge.DrawDepths;
import sps.bridge.EntityTypes;
import sps.core.Point2;
import sps.core.RNG;
import sps.entities.Entity;
import sps.text.TextEffects;
import sps.text.TextPool;

public class Catchable extends Entity {
    private static Player __player;
    private static final int __changeDirectionSecondsMax = 3;

    private Creature _creature;
    private float _dX = 0;
    private float _dY = 0;

    private float _changeDirectionsSeconds = 0;

    public Catchable(Player player) {
        __player = player;
        initialize(0, 0, Point2.Zero, null, EntityTypes.get("Catchable"), DrawDepths.get("Catchable"));
        _creature = new Creature();
        _creature.getBody().setScale(GameConfig.ArenaCreatureScale);
        _creature.orientX((GameConfig.DevFlipEnabled) ? RNG.coinFlip() : false, false);
        setSize(_creature.getWidth(), _creature.getHeight());
        setLocation(new Point2(RNG.next(Arena.getBounds().X, Arena.getBounds().X2), RNG.next(Arena.getBounds().Y, Arena.getBounds().Y2)));
    }

    private void interactWithPlayer() {
        if (__player.getNet().isTouching(_creature)) {
            if (__player.getPet() == null || __player.getPet().isLargerThan(_creature)) {
                _creature.getBody().setHighlight(Color.BLUE);
            }
            else {
                _creature.getBody().setHighlight(Color.RED);
            }
            if (__player.getNet().isInUse()) {
                __player.getNet().disable();
                if (__player.getPet() == null) {
                    _creature.getBody().setHighlight(Color.WHITE);
                    __player.setPet(_creature);
                    setInactive();
                    MusicPlayer.get(new SingleSongPlayer("Quickly.ogg"));
                    MusicPlayer.get().start();
                }
                else {
                    if (__player.getPet().isLargerThan(_creature)) {
                        //TODO Chomping sound effect here
                        TextPool.get().write("*CHOMP*", __player.getLocation(), 1f, TextEffects.Fountain);
                        Score.get().addChomp();
                        __player.getPet().addBonus(Math.min(__player.getPet().getStats().power() - _creature.getStats().power(), _creature.getStats().power()));
                        setInactive();
                    }
                    else {
                        __player.freeze();
                        _creature.addBonus(Math.min(_creature.getStats().power() - __player.getPet().getStats().power(), __player.getPet().getStats().power()));
                    }
                }
            }
        }
        else {
            _creature.getBody().setHighlight(Color.WHITE);
        }
    }

    private void movement() {
        if (GameConfig.DevFlipEnabled) {
            _creature.orientX(_dX <= 0, false);
        }

        _changeDirectionsSeconds -= Gdx.graphics.getDeltaTime();

        boolean anyPartOutside = _creature.getBody().anyPartOutsideArena(_dX, _dY);
        if (!anyPartOutside) {
            move(_dX, _dY);
        }

        if (_changeDirectionsSeconds <= 0 || anyPartOutside) {
            _changeDirectionsSeconds = RNG.next(__changeDirectionSecondsMax / 2, __changeDirectionSecondsMax);
            float playerSpeedPercent = .5f;
            _dX = RNG.next(-GameConfig.PlayerTopSpeed, GameConfig.PlayerTopSpeed) * playerSpeedPercent;
            _dY = RNG.next(-GameConfig.PlayerTopSpeed, GameConfig.PlayerTopSpeed) * playerSpeedPercent;
        }
        _dX = -GameConfig.PlayerTopSpeed * .5f;
        _dY = 0;
    }

    @Override
    public void update() {
        if (!_creature.getBody().isAlive()) {
            setInactive();
        }
        _creature.setLocation(getLocation());

        interactWithPlayer();
        movement();
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
}
