package game.population;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.core.GameConfig;
import sps.bridge.DrawDepths;
import sps.color.Color;
import sps.color.RGBA;
import sps.core.Point2;
import sps.core.RNG;
import sps.display.Window;
import sps.draw.ProcTextures;
import sps.draw.SpriteMaker;
import sps.draw.TextureManipulation;

public class Map {
    public static final int C = 255;
    private static final int __frameSizePixels = 6;

    private Sprite _sprite;
    private Sprite _bg;
    private int[][] _habitableZones;
    private Point2 _position;
    private Color[][] _spriteBase;
    private Color[][] _bgBase;

    private static final int __habitBuffer = 25;

    public Map(int width, int height, Point2 position) {
        _position = position;
        _spriteBase = ProcTextures.perlin(width, height, new RGBA(0, 255, 255).toColor(), new RGBA(255, 255, 255).toColor(), 9, true);
        _habitableZones = new int[width][height];

        if (GameConfig.OptSmoothRegionMap) {
            TextureManipulation.blurNaive(_spriteBase, 2);
        }

        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < height; jj++) {
                int elevation = (int) (255 * _spriteBase[ii][jj].r);
                _spriteBase[ii][jj] = Biome.getColor(elevation);
                if (ii > __habitBuffer && ii < width - __habitBuffer && jj > __habitBuffer && jj < height - __habitBuffer) {
                    _habitableZones[ii][jj] = Biome.fromElevation(elevation).Habitable ? 1 : 0;
                }
                else {
                    _habitableZones[ii][jj] = 0;
                }
            }
        }

        _bgBase = ProcTextures.monotone(width + __frameSizePixels, height + __frameSizePixels, Color.WHITE);

        regenerateTextures();
    }

    public void regenerateTextures() {
        _bg = SpriteMaker.fromColors(_bgBase);
        _sprite = SpriteMaker.fromColors(_spriteBase);

        _bg.setPosition(_position.X - __frameSizePixels / 2, _position.Y - __frameSizePixels / 2);
        _sprite.setPosition(_position.X, _position.Y);
    }

    public void resetSpace(Point2 location) {
        int x = (int) (location.X);
        int y = (int) (location.Y);
        _habitableZones[x][y] = 1;
    }

    private static final int requiredLivingSpace = 15;

    public Point2 getOpenSpace() {
        while (true) {
            Point2 location = new Point2(RNG.next(0, _habitableZones.length), RNG.next(0, _habitableZones[0].length));
            if (_habitableZones[(int) location.X][(int) location.Y] == 1) {
                boolean success = true;
                for (int ii = -requiredLivingSpace; ii < requiredLivingSpace; ii++) {
                    for (int jj = -requiredLivingSpace; jj < requiredLivingSpace; jj++) {
                        int x = (int) (ii + location.X);
                        int y = (int) (jj + location.Y);
                        if (x >= 0 && x < _habitableZones.length && y >= 0 && y < _habitableZones[0].length) {
                            if (_habitableZones[x][y] != 1) {
                                success = false;
                            }
                        }
                    }
                }
                if (success) {
                    _habitableZones[(int) location.X][(int) location.Y] = 2;
                    return location;
                }
            }
        }
    }

    public Point2 getPosition() {
        return _position;
    }

    public void draw() {
        Window.get().schedule(_bg, DrawDepths.get("PopulationMapBackground"));
        Window.get().schedule(_sprite, DrawDepths.get("PopulationMap"));
    }
}
