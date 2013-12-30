package sps.preload;

import sps.color.Colors;
import sps.display.Screen;
import sps.text.Text;
import sps.text.TextPool;
import sps.ui.Meter;

import java.text.NumberFormat;
import java.util.LinkedList;

public abstract class PreloadChain {
    private LinkedList<PreloadChainLink> _preloadChain;
    private boolean processStep = false;
    private int _preloadedItemsTarget = 0;
    private int _preloadedItems = 0;
    private Text _loadingMessage;
    private Meter _loadingMeter;

    public PreloadChain() {
        _preloadChain = new LinkedList<>();
        _loadingMessage = TextPool.get().write("", Screen.pos(10, 60));
        _loadingMeter = new Meter(90, 5, Colors.randomPleasant(), Screen.pos(5, 30), false);
    }

    public void add(PreloadChainLink link) {
        _preloadChain.add(link);
        _preloadedItemsTarget += link.getRepetitions();
    }

    public void update() {
        PreloadChainLink link = _preloadChain.peek();
        if (!processStep) {
            if (link == null) {
                finish();
            }
            else {
                _loadingMeter.setPercent((int) ((_preloadedItems / (float) _preloadedItemsTarget) * 100));
                _loadingMessage.setMessage(link.getMessage() + "\n" + getProgress());
            }
        }
        else {
            link.process();
            link.useLink();
            if (link.allLinksRun()) {
                _preloadChain.pop();
            }
            _preloadedItems++;
        }
        processStep = !processStep;
    }

    private static final NumberFormat _df = NumberFormat.getPercentInstance();

    private String getProgress() {
        return _df.format(((float) _preloadedItems / _preloadedItemsTarget)) + " complete";
    }

    public void draw() {
        _loadingMeter.draw();
    }

    public abstract void finish();
}
