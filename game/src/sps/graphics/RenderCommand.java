package sps.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import sps.core.Point2;

public class RenderCommand {
    public String Content;
    public Point2 Location;
    public Color Filter;
    public float Scale;

    public RenderCommand(String content, Point2 location, Color filter, float scale) {
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

    public RenderCommand(Sprite sprite, Point2 position, Color color, float width, float height, float scaleX, float scaleY) {
        Sprite = sprite;
        Location = position;
        Filter = color;
        Width = width;
        Height = height;
        ScaleX = scaleX;
        ScaleY = scaleY;
    }
}
