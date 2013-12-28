package game.population;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.core.GameConfig;
import sps.bridge.DrawDepths;
import sps.bridge.SpriteTypes;
import sps.core.Point2;
import sps.display.Assets;
import sps.display.SpriteInfo;
import sps.display.SpriteSheetManager;
import sps.display.Window;
import sps.util.Maths;

import java.util.ArrayList;
import java.util.List;

public class PopulationHUD {
    private Population _population;

    private Map _map;
    private Sprite _popIcon;
    private int _maxIconsWide;
    private int _maxIconsHigh;
    private int _maxIcons;
    private List<Point2> _iconPositions;
    private int _pad = 1;
    private Point2 _size;

    public PopulationHUD(Population population, Point2 size, Point2 position) {
        _size = size;
        _population = population;

        _map = new Map((int) size.X, (int) size.Y, position);

        _iconPositions = new ArrayList<>();
    }

    public void recalcIcons() {
        int iconsToDraw = Maths.clamp((int) (_maxIcons * ((float) _population.getSize()) / GameConfig.PopulationMax), 1, _maxIcons);

        while (iconsToDraw < _iconPositions.size()) {
            Point2 kill = _iconPositions.get(_iconPositions.size() - 1);
            _iconPositions.remove(_iconPositions.size() - 1);
            _map.resetSpace(kill.add(-_map.getPosition().X, -_map.getPosition().Y).add(_popIcon.getWidth() / 2, _popIcon.getHeight() / 2));
        }

        while (iconsToDraw > _iconPositions.size()) {
            _iconPositions.add(_map.getOpenSpace().add(_map.getPosition()).add(-_popIcon.getWidth() / 2, -_popIcon.getHeight() / 2));
        }
    }

    public void draw() {
        _map.draw();
        for (Point2 p : _iconPositions) {
            _popIcon.setPosition(p.X, p.Y);

            Window.get().schedule(_popIcon, DrawDepths.get("PopulationIcons"));
        }
    }

    public void regenerateTextures() {
        SpriteInfo popIconInfo = SpriteSheetManager.getSpriteInfo(SpriteTypes.get("Settlement"));
        _popIcon = Assets.get().sprite(popIconInfo.SpriteIndex);
        _popIcon.setSize(32, 32);
        _map.regenerateTextures();
        _maxIconsHigh = (int) (_size.Y / (_popIcon.getHeight() + _pad)) - 1;
        _maxIconsWide = (int) (_size.X / (_popIcon.getWidth() + _pad)) - 1;
        _maxIcons = _maxIconsHigh * _maxIconsWide;

    }
}
