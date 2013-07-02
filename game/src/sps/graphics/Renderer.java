package sps.graphics;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import sps.bridge.DrawDepth;
import sps.core.Logger;
import sps.core.Point2;
import sps.core.SpsConfig;
import sps.util.Screen;

public class Renderer {

    private static Renderer instance;
    private static RenderStrategy defaultStrategy = new StretchStrategy();
    private ApplicationListener refreshInstance;

    public static Renderer get() {
        if (instance == null) {
            int width = SpsConfig.get().virtualWidth;
            int height = SpsConfig.get().virtualHeight;
            Logger.info("Virtual resolution: " + width + "W, " + height + "H");
            instance = new Renderer(width, height);
            instance.setStrategy(defaultStrategy);
        }
        return instance;
    }

    private SpriteBatch _batch;
    private OrthographicCamera _camera;
    private RenderStrategy strategy;
    private Color bgColor;

    private Renderer(int width, int height) {
        _batch = new SpriteBatch();
        bgColor = Color.WHITE;
        strategy = new StretchStrategy();
        resize(width, height);
        setShader(Assets.get().defaultShaders());
    }

    public void setShader(ShaderProgram shader) {
        _batch.setShader(shader);
    }

    public void setRefreshInstance(ApplicationListener app) {
        refreshInstance = app;
    }

    public void setWindowsBackground(Color bgColor) {
        this.bgColor = bgColor;
    }

    private static boolean tipHasBeenDisplayed = false;

    public void setStrategy(RenderStrategy strategy) {
        this.strategy = strategy;
        _camera = strategy.createCamera();
        if (refreshInstance != null) {
            refreshInstance.resize(Screen.get().VirtualWidth, Screen.get().VirtualHeight);
        }
        else {
            if (!tipHasBeenDisplayed) {
                Logger.info("If the app is registered with Renderer.get().setRefreshInstance(this); in the create method, then the screen will update without a manual resizing.");
                tipHasBeenDisplayed = true;
            }
        }
    }

    public void toggleFullScreen() {
        Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, !Gdx.graphics.isFullscreen());
        resize(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height);
    }

    public void begin() {
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        _camera.update();
        strategy.begin(_camera, _batch);
        _batch.begin();
    }

    public void end() {
        _batch.end();
    }

    public void resize(int width, int height) {
        strategy.resize(width, height);
    }

    public void moveCamera(int x, int y) {
        _camera.translate(x * Gdx.graphics.getDeltaTime(), y * Gdx.graphics.getDeltaTime());
    }

    public Vector2 getBuffer() {
        return strategy.getBuffer();
    }

    // Sprite rendering
    public void draw(Sprite sprite, Point2 position, DrawDepth depth, Color color) {
        render(sprite, position, depth, color, SpsConfig.get().spriteWidth, SpsConfig.get().spriteHeight, 1, 1);
    }

    public void draw(Sprite sprite, Point2 position, DrawDepth depth, Color color, boolean flipX, boolean flipY) {
        render(sprite, position, depth, color, sprite.getWidth(), sprite.getHeight(), flipX ? -1 : 1, flipY ? -1 : 1);
    }

    public void draw(Sprite sprite, Point2 position, DrawDepth depth, Color color, float width, float height) {
        render(sprite, position, depth, color, width, height, 1, 1);
    }

    Point2 pos = new Point2(0, 0);

    public void draw(Sprite sprite) {
        pos.reset(sprite.getX(), sprite.getY());
        render(sprite, pos, null, sprite.getColor(), sprite.getWidth(), sprite.getHeight(), 1, 1);
    }

    private void render(Sprite sprite, Point2 position, DrawDepth depth, Color color, float width, float height, float scaleX, float scaleY) {
        sprite.setColor(color);
        sprite.setSize(width, height);
        sprite.setScale(scaleX, scaleY);
        sprite.setPosition(position.X, position.Y);
        sprite.draw(_batch);
    }

    // String rendering
    public void draw(String content, Point2 location, Color filter, float scale, DrawDepth depth) {
        renderString(content, location, filter, scale, depth);
    }

    private void renderString(String content, Point2 location, Color filter, float scale, DrawDepth depth) {
        Assets.get().font().setScale(scale);
        Assets.get().font().setColor(filter);
        Assets.get().font().draw(_batch, content, location.PosX, location.PosY);
    }

    //Texture rendering
    public void draw(Texture texture) {
        _batch.draw(texture, 0, 0);
    }
}
