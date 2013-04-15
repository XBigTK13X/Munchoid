package game.arena;

import game.Game;
import game.creatures.Stats;
import sps.bridge.*;
import sps.core.Point2;
import sps.entities.Entity;
import sps.io.Input;
import sps.util.Screen;

public class Player extends Entity{
    private Point2 _keyVelocity = new Point2(0,0);
    private float _moveDistance = (Screen.height(1) + Screen.width(1)) / 2;

    public Player(){
        initialize(Screen.pos(20,20), SpriteTypes.get("Player_Stand"), EntityTypes.get(Sps.Actors.Player), DrawDepths.get(Sps.Actors.Player));
    }

    @Override
    public void update(){
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

        move(_keyVelocity.X,_keyVelocity.Y);
    }
}
