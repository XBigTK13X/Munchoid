package sps.core;

import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.display.Screen;
import sps.display.Window;
import sps.draw.SpriteMaker;
import sps.text.Text;
import sps.text.TextPool;

public class DevConsole {
    private class ConsoleText {
        private Point2 position = new Point2(0, 0);
        private Text _content;


        public ConsoleText(int x, int y, String content) {
            this.position.reset(x, y);
            _content = TextPool.get().write(content, position);
            _content.setVisible(_isVisible);
            _content.setDepth(DrawDepths.get("DevConsoleText"));
        }

        public void draw() {
            if (_isVisible) {
                _content.draw();
            }
        }

        public Text getContent() {
            return _content;
        }

        public void setContent(String content) {
            _content.setMessage(content);
        }
    }

    public static final int __margin = 70;

    private static DevConsole __instance;

    public static DevConsole get() {
        if (__instance == null) {
            __instance = new DevConsole();
        }
        return __instance;
    }

    private final int messageLimit = 100;
    private final ConsoleText[] _contents = new ConsoleText[messageLimit];
    private int _index = 0;
    private boolean _isVisible;
    private final Color _bgColor;
    private final Sprite _consoleBase;

    private String _fontLabel;
    private int _pointSize;

    private DevConsole() {
        _bgColor = Color.BLACK.newAlpha(.75f);
        _consoleBase = SpriteMaker.pixel(Color.WHITE);
        _isVisible = false;
        add("The development console has been started.");
    }

    public void setFont(String fontLabel, int pointSize) {
        _fontLabel = fontLabel;
        _pointSize = pointSize;
        for (int ii = 0; ii < _contents.length - 1; ii++) {
            if (_contents[ii] != null) {
                _contents[ii].getContent().setFont(_fontLabel, _pointSize);
            }
        }
    }

    private int getY(int index) {
        return Screen.get().VirtualHeight - (index * __margin / 2) - 50;
    }

    public void add(String message) {
        if (_index < _contents.length) {
            _contents[_index] = new ConsoleText(__margin, getY(_index), message);
            _contents[_index].getContent().setFont(_fontLabel, _pointSize);
            _index++;
        }
        else {
            for (int ii = 0; ii < _contents.length - 1; ii++) {
                _contents[ii].setContent(_contents[ii + 1].getContent().getMessage());
            }
            _contents[_contents.length - 1].setContent(message);
        }
    }

    public void draw() {
        if (_isVisible) {
            _consoleBase.setSize(Screen.get().VirtualWidth, Screen.get().VirtualHeight);
            _consoleBase.setColor(_bgColor.getGdxColor());
            _consoleBase.setPosition(0, 0);
            Window.get(true).schedule(_consoleBase, DrawDepths.get("DevConsole"));
            for (ConsoleText _content : _contents) {
                if (_content != null) {
                    _content.getContent().setMoveable(false);
                    _content.draw();
                }
            }
        }
    }

    public void toggle() {
        if (SpsConfig.get().devConsoleEnabled) {
            _isVisible = !_isVisible;
            for (int ii = 0; ii < _contents.length; ii++) {
                if (_contents[ii] != null) {
                    _contents[ii].getContent().setVisible(_isVisible);
                }
            }
        }
    }
}
