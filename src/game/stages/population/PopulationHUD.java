package game.stages.population;

import com.badlogic.gdx.graphics.g2d.Sprite;
import config.GameConfig;
import sps.bridge.DrawDepths;
import sps.bridge.SpriteTypes;
import sps.core.Point2;
import sps.core.RNG;
import sps.display.Assets;
import sps.display.SpriteInfo;
import sps.display.SpriteSheetManager;
import sps.display.Window;
import sps.util.Maths;

import java.util.ArrayList;

public class PopulationHUD {
    private Population _population;

    private Map _map;
    private Sprite _popIcon;
    private int _maxIconsWide;
    private int _maxIconsHigh;
    private int _maxIcons;
    private ArrayList<Point2> _iconPositions;
    private int _pad = 1;
    private Point2 _size;

    public PopulationHUD(Population population, Point2 size, Point2 position, int mapSeed, ArrayList<Point2> iconPositions) {
        _size = size;
        _population = population;
        _map = new Map((int) size.X, (int) size.Y, position, mapSeed);
        _iconPositions = iconPositions;
    }

    //Returns the growth of settlements
    public int recalcIcons() {
        int possibleIcons = (int) (_maxIcons * ((float) _population.getSize()) / GameConfig.PopulationMax);
        int iconsToDraw = Maths.clamp(possibleIcons, 1, _maxIcons);

        int growth = iconsToDraw - _iconPositions.size();
        if (growth != 0) {

            while (iconsToDraw < _iconPositions.size()) {
                int choice = RNG.next(_iconPositions.size());
                Point2 kill = _iconPositions.get(choice);
                _iconPositions.remove(choice);
                _map.resetSpace(kill.add(-_map.getPosition().X, -_map.getPosition().Y).add(_popIcon.getWidth() / 2, _popIcon.getHeight() / 2));
            }

            while (iconsToDraw > _iconPositions.size()) {
                _iconPositions.add(_map.getOpenSpace().add(_map.getPosition()).add(-_popIcon.getWidth() / 2, -_popIcon.getHeight() / 2));
            }
        }
        return growth;
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

    public int getMapSeed() {
        return _map.getSeed();
    }

    public ArrayList<Point2> getSettlementLocations() {
        return _iconPositions;
    }
}
