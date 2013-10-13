package game.population;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.InputWrapper;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.SpriteMaker;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.CoolDown;

public class DeathCauseEradicated {
    private boolean _isActive;
    private Text _announcement;
    private Sprite _bg;

    private final CoolDown __displayTimer = new CoolDown(7);

    public DeathCauseEradicated(DeathCause top, DeathCause bottom) {
        _isActive = true;
        String notification = "Your Munchoid was strong enough to eradicate the following cause" + ((top != null) ? "s" : "") + " of death";
        notification += "\n" + ((top != null) ? top.getName() + " and " : "") + bottom.getName();
        notification += "\n Press " + Commands.get("Pass") + " to continue";
        _announcement = TextPool.get().write(notification, Screen.pos(10, 50));
        Color overlay = Color.BLACK;
        overlay.a = .6f;
        _bg = SpriteMaker.get().pixel(overlay);
        _bg.setSize(Screen.width(100), Screen.height(100));
    }

    public boolean isActive() {
        return _isActive;
    }

    public void update() {
        if (InputWrapper.pass()) {
            _isActive = false;
            _announcement.setVisible(false);
        }
    }

    public void draw() {
        Window.get().render(_bg, DrawDepths.get("DeathCauseResultScreen"));
    }
}
