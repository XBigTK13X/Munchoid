package game.arena;

import com.badlogic.gdx.Gdx;
import game.Game;
import game.GameConfig;
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
    private float _moveDistance = GameConfig.playerTopSpeed;

    private CatchNet _net;
    private Creature _pet;

    private Floor _floor;

    public Player(Floor floor) {
        __scrollSpeedX = GameConfig.playerTopSpeed;
        __scrollSpeedY = GameConfig.playerTopSpeed;
        _movementBuffer = Screen.pos(45, 45);

        _floor = floor;

        initialize(SpsConfig.get().spriteWidth, SpsConfig.get().spriteHeight, Screen.pos(20, 20), SpriteTypes.get("Player_Stand"), EntityTypes.get(Sps.Entities.Actor), DrawDepths.get(Sps.Actors.Player));
        _actorType = ActorTypes.get(Sps.Actors.Player);
        _net = new CatchNet(this);
        EntityManager.get().addEntity(_net);
        setLocation(Screen.pos(50, 50));
    }

    private boolean inXBuffer(float offset) {
        return getLocation().X + offset > _movementBuffer.X
                && getLocation().X + offset < Renderer.get().VirtualWidth - _movementBuffer.X;
    }

    private boolean inYBuffer(float offset) {
        return getLocation().Y + offset > _movementBuffer.Y
                && getLocation().Y + offset < Renderer.get().VirtualHeight - _movementBuffer.Y;
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
        calculateKeyVelocity();

        float adjX = _keyVelocity.X * Gdx.graphics.getDeltaTime();
        float adjY = _keyVelocity.Y * Gdx.graphics.getDeltaTime();

        boolean inBufferX = inXBuffer(0);
        boolean inBufferY = inYBuffer(0);

        boolean nextMoveInXBuffer = inXBuffer(adjX);
        boolean nextMoveInYBuffer = inYBuffer(adjY);


        if (nextMoveInXBuffer) {
            move(_keyVelocity.X, 0);
        }
        else {
            int oX = _keyVelocity.X == 0 ? 0 : (_keyVelocity.X > 0 ? __scrollSpeedX : -__scrollSpeedX);

            //Logger.devConsole("X->FL: os: " + Renderer.get().getXOffset() + ", marg: " + floor.MarginX + ", loc: " + floor.getLocation().X + " , add:" + (floor.getLocation().X - oX * Gdx.graphics.getDeltaTime()));
            if (_floor.getLocation().X - oX * Gdx.graphics.getDeltaTime() > _floor.MarginX * -2 && _floor.getLocation().X - oX * Gdx.graphics.getDeltaTime() < 0) {
                Renderer.get().moveOffsets(oX, 0);
            }
        }
        if (nextMoveInYBuffer) {
            move(0, _keyVelocity.Y);
        }
        else {

            int oY = _keyVelocity.Y == 0 ? 0 : _keyVelocity.Y > 0 ? __scrollSpeedY : -__scrollSpeedY;
            //Logger.devConsole("Y->FL: os: " + Renderer.get().getYOffset() + ", marg: " + floor.MarginY + ", loc: " + floor.getLocation().Y + " , add:" + (floor.getLocation().Y - oY * Gdx.graphics.getDeltaTime()));
            if (_floor.getLocation().Y - oY * Gdx.graphics.getDeltaTime() > _floor.MarginY * -2 && _floor.getLocation().Y - oY * Gdx.graphics.getDeltaTime() < 0) {
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
