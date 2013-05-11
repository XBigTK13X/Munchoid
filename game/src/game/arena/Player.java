package game.arena;

import com.badlogic.gdx.Gdx;
import game.Game;
import game.GameConfig;
import game.creatures.Creature;
import sps.bridge.*;
import sps.core.Logger;
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
    private float _moveDistance = GameConfig.playerTopSpeed;

    private CatchNet _net;
    private Creature _pet;

    public Player() {
        __scrollSpeedX = GameConfig.playerTopSpeed;
        __scrollSpeedY = GameConfig.playerTopSpeed;
        _movementBuffer = Screen.pos(45, 45);

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

        float adjX = _keyVelocity.X * Gdx.graphics.getDeltaTime();
        float adjY = _keyVelocity.Y * Gdx.graphics.getDeltaTime();

        boolean canMoveX = getLocation().X + adjX > _movementBuffer.X
                && getLocation().X + adjX < Renderer.get().VirtualWidth - _movementBuffer.X;

        boolean canMoveY = getLocation().Y + adjY > _movementBuffer.Y
                && getLocation().Y + adjY < Renderer.get().VirtualHeight - _movementBuffer.Y;

        if (canMoveX) {
            move(_keyVelocity.X, 0);
        }
        else {
            int oX = _keyVelocity.X == 0 ? 0 : (_keyVelocity.X > 0 ? __scrollSpeedX : -__scrollSpeedX);

            Floor floor = (Floor) EntityManager.get().getEntity(EntityTypes.get("Floor"));
            Logger.devConsole("X->FL: os: " + Renderer.get().getXOffset() + ", marg: " + floor.MarginX + ", loc: " + floor.getLocation().X + " , add:" + (floor.getLocation().X - oX * Gdx.graphics.getDeltaTime()));
            if (floor.getLocation().X - oX * Gdx.graphics.getDeltaTime() > floor.MarginX * -2 && floor.getLocation().X - oX * Gdx.graphics.getDeltaTime() < 0) {
                Renderer.get().moveOffsets(oX, 0);
            }
        }
        if (canMoveY) {
            move(0, _keyVelocity.Y);
        }
        else {
            Floor floor = (Floor) EntityManager.get().getEntity(EntityTypes.get("Floor"));
            int oY = _keyVelocity.Y == 0 ? 0 : _keyVelocity.Y > 0 ? __scrollSpeedY : -__scrollSpeedY;
            Logger.devConsole("Y->FL: os: " + Renderer.get().getYOffset() + ", marg: " + floor.MarginY + ", loc: " + floor.getLocation().Y + " , add:" + (floor.getLocation().Y - oY * Gdx.graphics.getDeltaTime()));
            if (floor.getLocation().Y - oY * Gdx.graphics.getDeltaTime() > floor.MarginY * -2 && floor.getLocation().Y - oY * Gdx.graphics.getDeltaTime() < 0) {
                Renderer.get().moveOffsets(0, oY);
            }
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
