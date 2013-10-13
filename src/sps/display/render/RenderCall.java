package sps.display.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.bridge.DrawDepth;
import sps.core.Point2;

public class RenderCall implements Comparable<RenderCall> {
    public String Content;
    public Point2 Location;
    public Color Filter;
    public int PointSize;
    public String FontLabel;
    public float Scale;
    public DrawDepth Depth;

    public RenderCall(String content, Point2 location, Color filter, String fontLabel, int pointSize, float scale, DrawDepth depth) {
        Content = content;
        Location = location;
        Filter = filter;
        PointSize = pointSize;
        FontLabel = fontLabel;
        Scale = scale;
        Depth = depth;
    }

    public com.badlogic.gdx.graphics.g2d.Sprite Sprite;

    public RenderCall(Sprite sprite, DrawDepth depth) {
        Sprite = sprite;
        Depth = depth;
    }

    @Override
    public int compareTo(RenderCall o) {
        return o.Depth.DrawDepth - Depth.DrawDepth;
    }
}
