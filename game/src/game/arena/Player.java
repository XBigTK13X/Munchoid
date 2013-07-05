package game.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import game.Game;
import game.GameConfig;
import game.creatures.Creature;
import sps.bridge.*;
import sps.core.Point2;
import sps.core.SpsConfig;
import sps.entities.Entity;
import sps.entities.IActor;
import sps.graphics.Window;
import sps.io.Input;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.util.Bounds;
import sps.util.Screen;

public class Player extends Entity implements IActor {
    private static int __scrollSpeedX;
    private static int __scrollSpeedY;

    private ActorType _actorType;

    private Point2 _movementBuffer;

    private Point2 _keyVelocity = new Point2(0, 0);
    private float _moveDistance = GameConfig.PlayerTopSpeed;

    private CatchNet _net;
    private Creature _pet;

    private Floor _floor;
    private float _frozenSeconds;
    private Arrow _arrow;

    public Player(Floor floor) {
        __scrollSpeedX = GameConfig.PlayerTopSpeed;
        __scrollSpeedY = GameConfig.PlayerTopSpeed;
        _movementBuffer = Screen.pos(45, 45);

        _floor = floor;

        initialize(SpsConfig.get().spriteWidth, SpsConfig.get().spriteHeight, Screen.pos(20, 20), SpriteTypes.get("Player_Stand"), EntityTypes.get(Sps.Entities.Actor), DrawDepths.get(Sps.Actors.Player));
        _actorType = ActorTypes.get(Sps.Actors.Player);
        _net = new CatchNet(this);
        _arrow = new Arrow(this);
        setLocation(Screen.pos(50, 50));
    }

    private boolean inXBuffer(float offset) {
        return getLocation().X + offset > _movementBuffer.X
                && getLocation().X + offset < Screen.get().VirtualWidth - _movementBuffer.X;
    }

    private boolean inYBuffer(float offset) {
        return getLocation().Y + offset > _movementBuffer.Y
                && getLocation().Y + offset < Screen.get().VirtualHeight - _movementBuffer.Y;
    }

    private void calculateKeyVelocity() {
        float leftVelocity = (Input.get().isActive(Commands.get(Game.CommandNames.MoveLeft)) ? -_moveDistance : 0);
        float rightVelocity = (Input.get().isActive(Commands.get(Game.CommandNames.MoveRight)) ? _moveDistance : 0);
        _keyVelocity.setX(rightVelocity + leftVelocity);
        if (_keyVelocity.X > 0) {
            setFacingLeft(false);
        }
        if (_keyVelocity.X < 0) {
            setFacingLeft(true);
        }
        float downVelocity = (Input.get().isActive(Commands.get(Game.CommandNames.MoveDown)) ? -_moveDistance : 0);
        float upVelocity = (Input.get().isActive(Commands.get(Game.CommandNames.MoveUp)) ? _moveDistance : 0);
        _keyVelocity.setY(upVelocity + downVelocity);
    }

    private void tryMoving(float x, float y) {
        float camX = Window.get().getCameraPosition().X + x * Gdx.graphics.getDeltaTime();
        float camY = Window.get().getCameraPosition().Y + y * Gdx.graphics.getDeltaTime();
        Bounds cam = Bounds.fromDimensions(camX, camY, Screen.get().VirtualWidth, Screen.get().VirtualHeight);
        Bounds arena = Bounds.fromDimensions(_floor.getLocation().X, _floor.getLocation().Y, _floor.getWidth(), _floor.getHeight());

        if (arena.envelopes(cam)) {
            Window.get().moveCamera((int) x, (int) y);
        }
        //TODO replace everything you deleted before. Only now use the above block instead of moving the offsets.
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

        tryMoving(_keyVelocity.X, 0);
        tryMoving(0, _keyVelocity.Y);

        if (Input.get().isActive(Commands.get("Confirm")) && !_net.isInUse()) {
            _net.use();
        }

        if (_pet != null) {
            _pet.update();
        }
    }

    public Creature getPet() {
        return _pet;
    }

    public void setPet(Creature pet) {
        _pet = pet;
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
