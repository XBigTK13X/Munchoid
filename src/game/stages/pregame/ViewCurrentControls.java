package game.stages.pregame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.bridge.Command;
import sps.bridge.Commands;
import sps.io.KeyCatcher;
import sps.io.Keys;
import sps.ui.ButtonStyle;
import sps.ui.UIButton;

import java.util.HashMap;
import java.util.Map;

public class ViewCurrentControls extends OptionsState {
    public ViewCurrentControls(Sprite background) {
        super(background);
    }

    private Map<String, String> commandNames;

    private void setupCommandNames() {
        commandNames = new HashMap<>();
        commandNames.put("Confirm", "Confirm");
        commandNames.put("MoveLeft", "Move Left");
        commandNames.put("MoveRight", "Move Right");
        commandNames.put("MoveUp", "Move Up");
        commandNames.put("MoveDown", "Move Down");
    }

    private String _chord;
    private KeyCatcher _catcher = new KeyCatcher() {
        @Override
        public void onDown(int keyCode) {
            _chord += Keys.find(keyCode) + "+";
        }
    };

    private void configure(Command command) {
        
    }

    @Override
    public void create() {
        setupCommandNames();
        ButtonStyle style = new ButtonStyle(5, 5, 30, 10, 10);
        int ii = 0;
        for (final String commandId : commandNames.keySet()) {
            final Command command = Commands.get(commandId);
            UIButton config = new UIButton(commandNames.get(commandId) + ": " + command) {
                @Override
                public void click() {
                    configure(command);
                }
            };
            style.apply(config, 0, 7 - ii++);
        }
    }

    @Override
    public void update() {

    }
}
