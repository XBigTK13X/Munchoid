package sps.display.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.core.Point2;

public class RenderCall {
    public String Content;
    public Point2 Location;
    public Color Filter;
    public int PointSize;
    public String FontLabel;
    public float Scale;

    public RenderCall(String content, Point2 location, Color filter, String fontLabel, int pointSize, float scale) {
        Content = content;
        Location = location;
        Filter = filter;
        PointSize = pointSize;
        FontLabel = fontLabel;
        Scale = scale;
    }

    public com.badlogic.gdx.graphics.g2d.Sprite Sprite;
    public float Width;
    public float Height;
    public float ScaleX;
    public float ScaleY;

    public RenderCall(Sprite sprite) {
        Sprite = sprite;
    }
}
