package sps.console;

import sps.bridge.DrawDepths;
import sps.core.Point2;
import sps.text.Text;
import sps.text.TextPool;

public class ConsoleText {
    private Point2 position = new Point2(0, 0);
    private Text _content;

    public ConsoleText(int x, int y, String content) {
        this.position.reset(x, y);
        _content = TextPool.get().write(content, position);
        _content.setDepth(DrawDepths.get("DevConsoleText"));
    }

    public void draw() {
        _content.draw();
    }

    public Text getContent() {
        return _content;
    }

    public void setContent(String content) {
        _content.setMessage(content);

    }
}
