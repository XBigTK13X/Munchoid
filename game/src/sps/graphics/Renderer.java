package sps.graphics;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import sps.bridge.DrawDepth;
import sps.core.Logger;
import sps.core.Point2;
import sps.core.SpsConfig;
import sps.util.Screen;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private static boolean __tipHasBeenDisplayed = false;

    private List<RenderCommand> _todo;
    private boolean _queueListening = false;

    private ApplicationListener _refreshInstance;
    private SpriteBatch _batch;
    private OrthographicCamera _camera;
    private RenderStrategy _strategy;
    private int _offsetX;
    private int _offsetY;


    public Renderer(int width, int height) {
        _todo = new ArrayList<RenderCommand>();
        _batch = new SpriteBatch();
        _strategy = new StretchStrategy();
        resize(width, height);
        setShader(Assets.get().defaultShaders());
    }

    public void setShader(ShaderProgram shader) {
        _batch.setShader(shader);
    }

    public void setRefreshInstance(ApplicationListener app) {
        _refreshInstance = app;
    }

    public void setStrategy(RenderStrategy strategy) {
        this._strategy = strategy;
        _camera = strategy.createCamera();
        if (_refreshInstance != null) {
            _refreshInstance.resize(Screen.get().VirtualWidth, Screen.get().VirtualHeight);
        }
        else {
            if (!__tipHasBeenDisplayed) {
                Logger.info("If the app is registered with Renderer.get().setRefreshInstance(this); in the create method, then the screen will update without a manual resizing.");
                __tipHasBeenDisplayed = true;
            }
        }
    }

    public void toggleFullScreen() {
        Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, !Gdx.graphics.isFullscreen());
        resize(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height);
    }

    public void begin() {
        _camera.update();
        _strategy.begin(_camera, _batch, _offsetX, _offsetY);
        _batch.begin();
    }

    public void end() {
        _batch.end();
    }

    public void resize(int width, int height) {
        _strategy.resize(width, height);
    }

    public void moveCamera(int x, int y) {
        _offsetX = _offsetX + (int) (x * Gdx.graphics.getDeltaTime());
        _offsetY = _offsetY + (int) (y * Gdx.graphics.getDeltaTime());
    }

    public Vector2 getBuffer() {
        return _strategy.getBuffer();
    }

    public void setListening(boolean listening) {
        _queueListening = listening;
    }

    public void processQueue() {
        setListening(false);
        _batch.begin();
        for (RenderCommand c : _todo) {
            if (c.Sprite != null) {
                render(c.Sprite, c.Location, c.Filter, c.Width, c.Height, c.ScaleX, c.ScaleY);
            }
            if (c.Content != null) {
                render(c.Content, c.Location, c.Filter, c.Scale);
            }
        }
        _batch.end();
        _todo.clear();
        setListening(true);
    }

    // Sprite rendering
    public void draw(Sprite sprite, Point2 position, DrawDepth depth, Color color) {
        render(sprite, position, color, SpsConfig.get().spriteWidth, SpsConfig.get().spriteHeight, 1, 1);
    }

    public void draw(Sprite sprite, Point2 position, DrawDepth depth, Color color, boolean flipX, boolean flipY) {
        render(sprite, position, color, sprite.getWidth(), sprite.getHeight(), flipX ? -1 : 1, flipY ? -1 : 1);
    }

    public void draw(Sprite sprite, Point2 position, DrawDepth depth, Color color, float width, float height) {
        render(sprite, position, color, width, height, 1, 1);
    }

    Point2 pos = new Point2(0, 0);

    public void draw(Sprite sprite) {
        pos.reset(sprite.getX(), sprite.getY());
        render(sprite, pos, sprite.getColor(), sprite.getWidth(), sprite.getHeight(), 1, 1);
    }

    private void render(Sprite sprite, Point2 position, Color color, float width, float height, float scaleX, float scaleY) {
        if (_queueListening) {
            _todo.add(new RenderCommand(sprite, position, color, width, height, scaleX, scaleY));
        }
        else {
            sprite.setColor(color);
            sprite.setSize(width, height);
            sprite.setScale(scaleX, scaleY);
            sprite.setPosition(position.X, position.Y);
            sprite.draw(_batch);
        }

    }

    // String rendering
    public void draw(String content, Point2 location, Color filter, float scale, DrawDepth depth) {
        render(content, location, filter, scale);
    }

    private void render(String content, Point2 location, Color filter, float scale) {
        if (_queueListening) {
            _todo.add(new RenderCommand(content, location, filter, scale));
        }
        else {
            Assets.get().font().setScale(scale);
            Assets.get().font().setColor(filter);
            Assets.get().font().draw(_batch, content, location.X, location.Y);
        }
    }
}
