package game.arena;

import game.Game;
import game.creatures.Creature;
import sps.bridge.*;
import sps.core.Point2;
import sps.core.SpsConfig;
import sps.entities.Entity;
import sps.entities.EntityManager;
import sps.entities.IActor;
import sps.graphics.Renderer;
import sps.io.Input;
import sps.util.Screen;

public class Player extends Entity implements IActor {
    private static int __scrollSpeedX;
    private static int __scrollSpeedY;

    private ActorType _actorType;

    private Point2 _movementBuffer;

    private Point2 _keyVelocity = new Point2(0, 0);
    private float _moveDistance = 250;

    private CatchNet _net;
    private Creature _pet;

    public Player() {
        __scrollSpeedX = (int) Screen.width(2);
        __scrollSpeedY = (int) Screen.height(2);
        _movementBuffer = Screen.pos(33, 33);

        initialize(SpsConfig.get().spriteWidth, SpsConfig.get().spriteHeight, Screen.pos(20, 20), SpriteTypes.get("Player_Stand"), EntityTypes.get(Sps.Entities.Actor), DrawDepths.get(Sps.Actors.Player));
        _actorType = ActorTypes.get(Sps.Actors.Player);
        _net = new CatchNet(this);
        EntityManager.get().addEntity(_net);
        setLocation(Screen.pos(50, 50));
    }

    @Override
    public void update() {
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

        if (getLocation().X + _keyVelocity.X > _movementBuffer.X
                && getLocation().X + _keyVelocity.X < Renderer.get().VirtualWidth - _movementBuffer.X
                && getLocation().X + _keyVelocity.Y > _movementBuffer.Y
                && getLocation().X + _keyVelocity.Y < Renderer.get().VirtualWidth - _movementBuffer.Y) {
            move(_keyVelocity.X, _keyVelocity.Y);
        }
        else {
            int oX = _keyVelocity.X == 0 ? 0 : _keyVelocity.X > 0 ? __scrollSpeedX : -__scrollSpeedX;
            int oY = _keyVelocity.Y == 0 ? 0 : _keyVelocity.Y > 0 ? __scrollSpeedY : -__scrollSpeedY;
            Renderer.get().moveOffsets(oX, oY);
        }


        if (Input.get().isActive(Commands.get("Confirm"))) {
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
}
