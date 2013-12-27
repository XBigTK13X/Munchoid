package game.population;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.InputWrapper;
import sps.bridge.Commands;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.color.Colors;
import sps.core.RNG;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.SpriteMaker;
import sps.particles.ParticleWrapper;
import sps.text.Text;
import sps.text.TextPool;
import sps.util.CoolDown;

public class DeathCauseEradicated {
    private boolean _isActive;
    private Text _announcement;
    private Sprite _bg;

    private final CoolDown __displayTimer = new CoolDown(7);
    private final CoolDown _fireWorksTimer = new CoolDown(.5f);

    public DeathCauseEradicated(DeathCause top, DeathCause bottom) {
        _isActive = true;
        String notification = "Your Munchoid was strong enough to eradicate the following cause" + ((top != null) ? "s" : "") + " of death.";
        notification += "\n\n\t" + ((top != null) ? top.getName() + " and " : "") + bottom.getName();
        notification += "\n\nPress " + Commands.get("Pass") + " to continue";
        _announcement = TextPool.get().write(notification, Screen.pos(10, 50));
        _announcement.setDepth(DrawDepths.get("DeathCauseResultText"));
        Color overlay = Color.BLACK.newAlpha(.90f);
        _bg = SpriteMaker.pixel(overlay);
        _bg.setSize(Screen.width(100), Screen.height(100));
    }

    public boolean isActive() {
        return _isActive;
    }

    public void update() {
        if (_fireWorksTimer.updateAndCheck()) {
            _fireWorksTimer.reset(RNG.next(50, 150) / 200f);
            ParticleEffect effect = ParticleWrapper.get().emit("fireworks", Screen.rand(5, 95, 5, 95), DrawDepths.get("Fireworks"));
            ParticleWrapper.setColor(effect, Colors.randomPleasant());
        }
        if (InputWrapper.pass()) {
            _isActive = false;
            _announcement.setVisible(false);
        }
    }

    public void draw() {
        Window.get().schedule(_bg, DrawDepths.get("DeathCauseResultScreen"));
    }
}
