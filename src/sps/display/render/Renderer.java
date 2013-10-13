package sps.display.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import game.GameConfig;
import sps.bridge.DrawDepth;
import sps.core.Point2;
import sps.display.Assets;
import sps.display.DrawAPICall;
import sps.display.Screen;
import sps.draw.DrawAPI;
import sps.entities.HitTest;
import sps.particles.ParticleLease;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private List<RenderCall> _todo;
    private boolean _storeAllCalls;

    private List<DrawAPICall> _drawApiCalls;

    private SpriteBatch _batch;
    private OrthographicCamera _camera;
    private RenderStrategy _strategy;
    private Point2 _offset = new Point2(0, 0);


    public Renderer(int width, int height) {
        _drawApiCalls = new ArrayList<>();
        _todo = new ArrayList<>();
        _batch = new SpriteBatch();
        _strategy = new StretchStrategy();
        _storeAllCalls = true;

        resize(width, height);
        setShader(Assets.get().defaultShaders());
    }

    public void setShader(ShaderProgram shader) {
        _batch.setShader(shader);
    }

    public void setStrategy(RenderStrategy strategy) {
        this._strategy = strategy;
        _camera = strategy.createCamera();

    }

    public void begin() {
        _camera.update();
        DrawAPI.get().update(_camera.combined, _batch.getProjectionMatrix());
        _strategy.begin(_camera, _batch, (int) _offset.X, (int) _offset.Y);
        _batch.begin();
    }

    public void end() {
        _batch.end();
    }

    public void resize(int width, int height) {
        _strategy.resize(width, height);
    }

    public void moveCamera(int x, int y) {
        if (x != 0 || y != 0) {
            _offset.X = _offset.X + (int) (x * Gdx.graphics.getDeltaTime());
            _offset.Y = _offset.Y + (int) (y * Gdx.graphics.getDeltaTime());
        }
    }

    public Point2 getCameraPosition() {
        return _offset;
    }

    public boolean canMoveCamera(float x, float y) {
        float x2 = _camera.position.x + x;
        float y2 = _camera.position.y + y;
        return HitTest.inBox((int) x2, (int) y2, 0, 0, Screen.get().VirtualWidth, Screen.get().VirtualHeight);
    }

    public void setCameraX(int x) {
        _offset.setX(x);
    }

    public void setCameraY(int y) {
        _offset.setY(y);
    }

    public void resetCamera() {
        _offset.setY(0);
        _offset.setX(0);
    }

    public OrthographicCamera getCamera() {
        return _camera;
    }

    public SpriteBatch getBatch() {
        return _batch;
    }

    public Vector2 getBuffer() {
        return _strategy.getBuffer();
    }

    public void setStoreAllCalls(boolean listening) {
        _storeAllCalls = listening;
    }

    public void processRenderCalls() {
        //While listening, calls are recorded and stored.
        //When not listening, calls are painted to the Window
        Collections.sort(_todo);
        setStoreAllCalls(false);
        begin();
        for (RenderCall command : _todo) {
            //Sprite render call
            if (command.Sprite != null) {
                render(command.Sprite, command.Depth);
            }
            //Text render call
            else if (command.Content != null) {
                renderLine(command.Content, command.Location, command.Filter, command.FontLabel, command.PointSize, command.Scale, command.Depth);
            }
            else if (command.Particles != null) {
                render(command.Particles, command.Depth);
            }
        }
        end();
        _todo.clear();
        setStoreAllCalls(true);
    }

    public void processDrawAPICalls() {
        //Draw API calls
        for (DrawAPICall call : _drawApiCalls) {
            DrawAPI.get().setColor(call.Color);
            if (call.Radius == null) {
                DrawAPI.get().line(call.X, call.Y, call.X2, call.Y2);
            }
            else {
                DrawAPI.get().circle(call.X, call.Y, call.Radius);
            }
        }
        _drawApiCalls.clear();
        setStoreAllCalls(true);
    }

    public void schedule(DrawAPICall apiCall) {
        _drawApiCalls.add(apiCall);
    }

    public void render(Sprite sprite, DrawDepth depth) {
        if (_storeAllCalls) {
            _todo.add(new RenderCall(sprite, depth));
        }
        else {
            sprite.draw(_batch);
        }
    }

    // String rendering
    public void render(String content, Point2 location, Color filter, String fontLabel, int pointSize, float scale, DrawDepth depth) {
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

    private BitmapFont _nextToWrite;

    private void renderLine(String content, Point2 location, Color filter, String fontLabel, int pointSize, float scale, DrawDepth depth) {
        if (_storeAllCalls) {
            _todo.add(new RenderCall(content, location, filter, fontLabel, pointSize, scale, depth));
        }
        else {
            _nextToWrite = Assets.get().fontPack().getFont(fontLabel, pointSize);
            _nextToWrite.setScale(scale);

            if (GameConfig.OptEnableFontOutlines) {
                //FIXME Really messy. Distance field fonts might remove the need for an outline.
                _nextToWrite.setColor(0, 0, 0, 1);

                int offset = 2;
                _nextToWrite.draw(_batch, content, location.X + offset, location.Y);
                _nextToWrite.draw(_batch, content, location.X - offset, location.Y);
                _nextToWrite.draw(_batch, content, location.X, location.Y + offset);
                _nextToWrite.draw(_batch, content, location.X, location.Y - offset);
            }

            _nextToWrite.setColor(filter);
            _nextToWrite.draw(_batch, content, location.X, location.Y);
        }
    }

    public void render(ParticleLease lease, DrawDepth depth) {
        if (_storeAllCalls) {
            _todo.add(new RenderCall(lease, depth));
        }
        else {
            lease.Effect.draw(getBatch(), Gdx.graphics.getDeltaTime());
        }
    }
}
