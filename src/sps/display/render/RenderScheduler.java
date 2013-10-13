package sps.display.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.bridge.DrawDepth;
import sps.core.Point2;
import sps.display.Assets;
import sps.display.DrawAPICall;
import sps.particles.ParticleLease;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderScheduler {
    private List<RenderCall> _renderApiCalls;
    private List<DrawAPICall> _drawApiCalls;

    public RenderScheduler() {
        _drawApiCalls = new ArrayList<>();
        _renderApiCalls = new ArrayList<>();
    }

    public List<DrawAPICall> getDrawApiCalls() {
        return _drawApiCalls;
    }

    public List<RenderCall> getRenderApiCalls() {
        return _renderApiCalls;
    }

    public void schedule(DrawAPICall apiCall) {
        _drawApiCalls.add(apiCall);
    }

    public void schedule(Sprite sprite, DrawDepth depth) {
        _renderApiCalls.add(new RenderCall(sprite, depth));
    }

    public void schedule(String content, Point2 location, Color filter, String fontLabel, int pointSize, float scale, DrawDepth depth) {
        if (content.contains("\n")) {
            int line = 0;
            if (pointSize == 0) {
                pointSize = Assets.get().fontPack().getDefaultPointSize();
            }
            for (String s : content.split("\n")) {
                renderLine(s, location.add(0, line++ * -pointSize), filter, fontLabel, pointSize, scale, depth);
            }
        }
        else {
            renderLine(content, location, filter, fontLabel, pointSize, scale, depth);
        }
    }

    private void renderLine(String content, Point2 location, Color filter, String fontLabel, int pointSize, float scale, DrawDepth depth) {
        _renderApiCalls.add(new RenderCall(content, location, filter, fontLabel, pointSize, scale, depth));
    }

    public void schedule(ParticleLease lease, DrawDepth depth) {
        _renderApiCalls.add(new RenderCall(lease, depth));
    }

    public void clear() {
        _renderApiCalls.clear();
        _drawApiCalls.clear();
    }

    public void sort() {
        Collections.sort(_renderApiCalls);
    }
}
