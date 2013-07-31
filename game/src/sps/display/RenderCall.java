package sps.display;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.core.Point2;

public class RenderCall {
    public String Content;
    public Point2 Location;
    public Color Filter;
    public float Scale;

    public RenderCall(String content, Point2 location, Color filter, float scale) {
        Content = content;
        Location = location;
        Filter = filter;
        Scale = scale;
    }

    public com.badlogic.gdx.graphics.g2d.Sprite Sprite;
    public float Width;
    public float Height;
    public float ScaleX;
    public float ScaleY;

    public RenderCall(Sprite sprite, Point2 position, Color color, float width, float height, float scaleX, float scaleY) {
        Sprite = sprite;
        Location = position;
        Filter = color;
        Width = width;
        Height = height;
        ScaleX = scaleX;
        ScaleY = scaleY;
    }
}