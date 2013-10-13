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
import sps.display.DrawApiCall;
import sps.display.Screen;
import sps.draw.DrawAPI;
import sps.entities.HitTest;
import sps.particles.ParticleLease;

public class ScreenRenderEngine {
    private SpriteBatch _batch;
    private OrthographicCamera _camera;
    private RenderStrategy _strategy;

    private Point2 _offset = new Point2(0, 0);

    public ScreenRenderEngine(int width, int height) {
        _batch = new SpriteBatch();
        _strategy = new StretchStrategy();
        resize(width, height);
        setShader(Assets.get().defaultShaders());
    }

    public void render(Sprite sprite, DrawDepth depth) {
        sprite.draw(_batch);
    }

    private BitmapFont _nextToWrite;

    public void render(String content, Point2 location, Color filter, String fontLabel, int pointSize, float scale, DrawDepth depth) {
        _nextToWrite = Assets.get().fontPack().getFont(fontLabel, pointSize);
        _nextToWrite.setScale(scale);
        content = content.replaceAll("\t", "    ");

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

    public void render(ParticleLease lease, DrawDepth depth) {
        lease.Effect.draw(getBatch(), Gdx.graphics.getDeltaTime());
    }

    public void render(DrawApiCall call) {
        DrawAPI.get().setColor(call.Color);
        if (call.Radius == null) {
            DrawAPI.get().line(call.X, call.Y, call.X2, call.Y2);
        }
        else {
            DrawAPI.get().circle(call.X, call.Y, call.Radius);
        }
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
}
