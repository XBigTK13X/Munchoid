package game.stages.population;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import game.app.core.InputWrapper;
import game.app.config.UIConfig;
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
    private boolean _active;
    private Text _announcement;
    private Text _diseases;
    private Text _continuePrompt;
    private Sprite _bg;
    private boolean _win;

    private final CoolDown _fireWorksTimer = new CoolDown(.5f);

    public DeathCauseEradicated(DeathCause top, DeathCause bottom) {
        _win = top != null;
        _active = true;
        _announcement = TextPool.get().write("You " + (_win ? "won" : "lost") + " this tournament.\nYour Munchoid was used to solve the following cause" + ((_win) ? "s" : "") + " of death.", UIConfig.EradicationAnnouncementPosition());
        _diseases = TextPool.get().write(((_win) ? top.getName() + " + " : "") + bottom.getName(), Screen.pos(10, 50));
        _continuePrompt = TextPool.get().write("Press " + Commands.get("Pass") + " to continue", Screen.pos(35, 15));

        _announcement.setDepth(DrawDepths.get("DeathCauseResultText"));
        _diseases.setDepth(DrawDepths.get("DeathCauseResultText"));
        _continuePrompt.setDepth(DrawDepths.get("DeathCauseResultText"));

        Color overlay = Color.BLACK.newAlpha(.90f);
        _bg = SpriteMaker.pixel(overlay);
        _bg.setSize(Screen.width(100), Screen.height(100));
    }

    public boolean isActive() {
        return _active;
    }

    public void update() {
        if (_win && _fireWorksTimer.updateAndCheck()) {
            _fireWorksTimer.reset(RNG.next(50, 150) / 200f);
            ParticleEffect effect = ParticleWrapper.get().emit("fireworks", Screen.rand(5, 95, 5, 95), DrawDepths.get("Fireworks"));
            ParticleWrapper.setColor(effect, Colors.randomPleasant());
        }
        if (InputWrapper.pass()) {
            _active = false;
            _announcement.setVisible(false);
            _continuePrompt.setVisible(false);
            _diseases.setVisible(false);
        }
    }

    public void draw() {
        Window.get().schedule(_bg, DrawDepths.get("DeathCauseResultScreen"));
    }
}
