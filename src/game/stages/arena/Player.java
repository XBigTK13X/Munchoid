package game.stages.arena;

import com.badlogic.gdx.Gdx;
import game.app.core.ArenaScore;
import game.config.GameConfig;
import game.app.core.InputWrapper;
import game.config.UIConfig;
import game.stages.common.creatures.Creature;
import game.app.dev.DevConfig;
import sps.audio.MusicPlayer;
import sps.audio.RandomSoundPlayer;
import sps.bridge.*;
import sps.color.Color;
import sps.core.Point2;
import sps.core.SpsConfig;
import sps.display.Screen;
import sps.display.Window;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.entities.IActor;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.util.Maths;

import java.util.List;

public class Player extends Entity implements IActor {
    private ActorType _actorType;

    private Point2 _keyVelocity = new Point2(0, 0);
    private float _moveDistance = GameConfig.PlayerTopSpeed;

    private CatchNet _net;
    private Creature _pet;

    private Floor _floor;
    private float _frozenSeconds;
    private Arrow _arrow;

    private Color _closestColor;

    public Player(Floor floor) {
        _floor = floor;

        initialize(SpsConfig.get().spriteWidth, SpsConfig.get().spriteHeight, Screen.pos(20, 20), SpriteTypes.get("Player_Stand"), EntityTypes.get(Sps.Entities.Actor), DrawDepths.get("Player"));
        _actorType = ActorTypes.get(Sps.Actors.Player);
        _net = new CatchNet(this);
        _arrow = new Arrow(this);
        setLocation(Screen.pos(50, 50));
    }

    private void calculateKeyVelocity() {
        if (DevConfig.BotEnabled) {
            Entity e = _arrow.getClosest();
            if (e != null) {
                float xVel = (e.getLocation().X - getLocation().X > 0) ? _moveDistance : -_moveDistance;
                float yVel = (e.getLocation().Y - getLocation().Y > 0) ? _moveDistance : -_moveDistance;
                _keyVelocity.reset(xVel, yVel);
                _closestColor = ((Catchable) e).getCreature().getBody().getHighlight();
            }
        }
        else {
            float leftVelocity = InputWrapper.moveLeft() ? -_moveDistance : 0;
            float rightVelocity = InputWrapper.moveRight() ? _moveDistance : 0;
            _keyVelocity.setX(rightVelocity + leftVelocity);
            float downVelocity = InputWrapper.moveDown() ? -_moveDistance : 0;
            float upVelocity = InputWrapper.moveUp() ? _moveDistance : 0;
            _keyVelocity.setY(upVelocity + downVelocity);
        }
        if (_keyVelocity.X > 0) {
            setFacingLeft(false);
        }
        if (_keyVelocity.X < 0) {
            setFacingLeft(true);
        }
    }


    public void moveInBothDirections(float x, float y) {
        //X Movement
        float adjustedXVelocity = x * Gdx.graphics.getDeltaTime();
        float nextX = getLocation().X + adjustedXVelocity;

        if (adjustedXVelocity > 0 && nextX < _floor.getBounds().Width - getWidth() || adjustedXVelocity < 0 && nextX > 0) {
            move(x, 0);
            float camX = getLocation().X - Screen.get().VirtualWidth / 2;
            Window.get().screenEngine().setCameraX(Maths.clamp(camX, 0, _floor.getBounds().Width - Screen.get().VirtualWidth));
        }

        //Y Movement
        float adjustedYVelocity = y * Gdx.graphics.getDeltaTime();
        float nextY = getLocation().Y + adjustedYVelocity;

        if (adjustedYVelocity > 0 && nextY < _floor.getBounds().Height - getHeight() || adjustedYVelocity < 0 && nextY > 0) {
            move(0, y);
            float camY = getLocation().Y - Screen.get().VirtualHeight / 2;
            Window.get().screenEngine().setCameraY(Maths.clamp(camY, 0, _floor.getBounds().Height - Screen.get().VirtualHeight));
        }
    }

    //Only used for chomp detections to avoid lots of garbage collecting
    Creature _creature;
    List<Entity> _catchables;

    private void interactWithCatchables() {
        _catchables = EntityManager.get().getEntities(EntityTypes.get("Catchable"));
        int chompCount = 0;
        for (Entity e : _catchables) {
            _creature = ((Catchable) e).getCreature();
            if (_net.isTouching(_creature) && _net.isInUse()) {
                if (_pet == null) {
                    _creature.getBody().setHighlight(Color.WHITE);
                    setPet(_creature);
                    e.setInactive();
                    MusicPlayer.get().play("Quickly", false);
                    _net.disable();
                    break;
                }
                else {
                    if (_pet.isLargerThan(_creature)) {
                        RandomSoundPlayer.play("chomp");
                        if (UIConfig.EnableChompText) {
                            TextPool.get().write("*CHOMP*", getLocation(), 1f, TextEffects.Fountain);
                        }
                        ArenaScore.get().addChomp();
                        _pet.addBonus(GameConfig.ChompPoints);
                        e.setInactive();
                        chompCount++;
                        _net.disable();
                        break;
                    }
                    else {
                        _creature.addBonus(GameConfig.ChompPoints);
                        chompCount--;
                    }
                }
            }
        }
        if (chompCount < 0) {
            _net.disable();
            freeze();
        }
    }

    @Override
    public void update() {
        if (_frozenSeconds > 0) {
            _frozenSeconds -= Gdx.graphics.getDeltaTime();
            if (_frozenSeconds <= 0) {
                _graphic.setColor(Color.WHITE);
            }
            return;
        }
        calculateKeyVelocity();

        moveInBothDirections(_keyVelocity.X, 0);
        moveInBothDirections(0, _keyVelocity.Y);

        boolean botCanSwing = DevConfig.BotEnabled && _closestColor == Catchable.CanBeCaughtHighlight;
        if ((botCanSwing || InputWrapper.confirm()) && !_net.isInUse()) {
            _net.use();
        }

        interactWithCatchables();

        if (_pet != null) {
            _pet.update();
        }
    }

    public Creature getPet() {
        return _pet;
    }

    public void setPet(Creature pet) {
        _pet = pet;
        _pet.setOwner(this);
    }

    @Override
    public ActorType getActorType() {
        return _actorType;
    }

    @Override
    public void performInteraction() {
    }

    public void freeze() {
        _frozenSeconds = GameConfig.PlayerFrozenSecondsMax;
        _graphic.setColor(Color.BLUE);
        TextPool.get().write("*FROZEN*", getLocation(), .5f, TextEffects.Fountain);
    }

    public CatchNet getNet() {
        return _net;
    }

    public Arrow getArrow() {
        return _arrow;
    }
}
