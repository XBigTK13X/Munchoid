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
import sps.io.Input;
import sps.text.TextEffects;
import sps.text.TextPool;
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

        float adjustedXVelocity = _keyVelocity.X * Gdx.graphics.getDeltaTime();
        boolean inBufferX = inXBuffer(0);
        boolean willBeInBufferX = inXBuffer(adjustedXVelocity);
        float nextX = getLocation().X + adjustedXVelocity;

        int floorVelocityX = _keyVelocity.X == 0 ? 0 : (_keyVelocity.X > 0 ? __scrollSpeedX : -__scrollSpeedX);
        float nextFloorX = _floor.getLocation().X - floorVelocityX * Gdx.graphics.getDeltaTime();
        if (willBeInBufferX) {
            move(_keyVelocity.X, 0);
        }
        else {
            if (_floor.canMoveToX(nextFloorX) && inBufferX) {
                //TODO Move the camera
            }
            else {
                if (adjustedXVelocity > 0 && nextX < Screen.get().VirtualWidth - getWidth() || adjustedXVelocity < 0 && nextX > 0) {
                    move(_keyVelocity.X, 0);
                }
            }
        }

        float adjustedYVelocity = _keyVelocity.Y * Gdx.graphics.getDeltaTime();
        boolean inBufferY = inYBuffer(0);
        int floorVelocityY = _keyVelocity.Y == 0 ? 0 : _keyVelocity.Y > 0 ? __scrollSpeedY : -__scrollSpeedY;
        float nextFloorY = _floor.getLocation().Y - floorVelocityY * Gdx.graphics.getDeltaTime();
        float nextY = getLocation().Y + adjustedYVelocity;

        if (inYBuffer(adjustedYVelocity)) {
            move(0, _keyVelocity.Y);
        }
        else {
            if (_floor.canMoveToY(nextFloorY) && inBufferY) {
                //TODO Move the camera
            }
            else {
                if (adjustedYVelocity > 0 && nextY < Screen.get().VirtualHeight - getHeight() || adjustedYVelocity < 0 && nextY > 0) {
                    move(0, _keyVelocity.Y);
                }
            }
        }

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
