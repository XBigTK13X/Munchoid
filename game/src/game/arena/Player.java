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
import sps.display.Window;
import sps.io.Input;
import sps.text.TextEffects;
import sps.text.TextPool;
import sps.util.MathHelper;
import sps.display.Screen;

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

    private void calculateKeyVelocity() {
        if (GameConfig.DevBotEnabled) {
            Entity e = _arrow.getClosest();
            if (e != null) {
                float xVel = (e.getLocation().X - getLocation().X > 0) ? _moveDistance : -_moveDistance;
                float yVel = (e.getLocation().Y - getLocation().Y > 0) ? _moveDistance : -_moveDistance;
                _keyVelocity.reset(xVel, yVel);
            }
        }
        else {
            float leftVelocity = (Input.get().isActive(Commands.get(Game.CommandNames.MoveLeft)) ? -_moveDistance : 0);
            float rightVelocity = (Input.get().isActive(Commands.get(Game.CommandNames.MoveRight)) ? _moveDistance : 0);
            _keyVelocity.setX(rightVelocity + leftVelocity);
            float downVelocity = (Input.get().isActive(Commands.get(Game.CommandNames.MoveDown)) ? -_moveDistance : 0);
            float upVelocity = (Input.get().isActive(Commands.get(Game.CommandNames.MoveUp)) ? _moveDistance : 0);
            _keyVelocity.setY(upVelocity + downVelocity);
        }
        if (_keyVelocity.X > 0) {
            setFacingLeft(false);
        }
        if (_keyVelocity.X < 0) {
            setFacingLeft(true);
        }
    }


    private void moveInBothDirections(float x, float y) {
        //X Movement
        float adjustedXVelocity = x * Gdx.graphics.getDeltaTime();
        float nextX = getLocation().X + adjustedXVelocity;

        if (adjustedXVelocity > 0 && nextX < _floor.getBounds().Width - getWidth() || adjustedXVelocity < 0 && nextX > 0) {
            move(x, 0);
            float camX = getLocation().X - Screen.get().VirtualWidth / 2;
            Window.get().setCameraX(MathHelper.clamp(camX, 0, _floor.getBounds().Width - Screen.get().VirtualWidth));
        }

        //Y Movement
        float adjustedYVelocity = y * Gdx.graphics.getDeltaTime();
        float nextY = getLocation().Y + adjustedYVelocity;

        if (adjustedYVelocity > 0 && nextY < _floor.getBounds().Height - getHeight() || adjustedYVelocity < 0 && nextY > 0) {
            move(0, y);
            float camY = getLocation().Y - Screen.get().VirtualHeight / 2;
            Window.get().setCameraY(MathHelper.clamp(camY, 0, _floor.getBounds().Height - Screen.get().VirtualHeight));
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

        if ((GameConfig.DevBotEnabled || Input.get().isActive(Commands.get("Confirm"))) && !_net.isInUse()) {
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
