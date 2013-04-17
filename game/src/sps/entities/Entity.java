package sps.entities;

import sps.bridge.DrawDepth;
import sps.bridge.EntityType;
import sps.bridge.SpriteType;
import sps.core.Point2;
import sps.core.SpsConfig;
import sps.graphics.Animation;
import sps.graphics.Renderer;
import sps.graphics.SpriteEdge;
import sps.graphics.SpriteInfo;
import sps.util.Screen;

public class Entity implements Comparable {

    private static int isNeg = 1;
    private static int factorsOfSpriteHeight = 0;
    protected final Animation _graphic = new Animation();
    protected final Point2 _location = new Point2(0, 0);
    private final Point2 oldLocation = new Point2(0, 0);
    private final Point2 target = new Point2(0, 0);
    protected boolean _isActive = true;
    protected Boolean _isBlocking;
    protected SpriteType _assetName;
    protected boolean _isOnBoard = true;
    protected EntityType _entityType;
    private boolean _isInteracting = false;
    private boolean facingLeft = true;

    private static float normalizeDistance(float amount) {
        isNeg = (amount < 0) ? -1 : 1;
        amount = Math.abs(amount);
        factorsOfSpriteHeight = (int) Math.floor(amount / SpsConfig.get().spriteHeight);
        factorsOfSpriteHeight = (factorsOfSpriteHeight == 0 && amount != 0) ? 1 : factorsOfSpriteHeight;
        return (SpsConfig.get().spriteHeight * factorsOfSpriteHeight * isNeg);
    }

    public void loadContent() {
        _graphic.loadContent(_assetName);
    }

    public void draw() {
        if (_isOnBoard && _isActive) {
            _graphic.draw();
        }
    }

    public void hide() {
        _isOnBoard = false;
    }

    public void show() {
        _isOnBoard = true;
    }

    protected void initialize(Point2 location, SpriteType spriteType, EntityType entityType, DrawDepth depth) {
        _assetName = spriteType;
        _entityType = entityType;
        _location.copy(location);
        _graphic.setPosition(_location);
        _graphic.setDrawDepth(depth);
        _graphic.loadContent(spriteType);
    }

    public void update() {
    }

    public void updateLocation(Point2 location) {
        oldLocation.copy(_location);
        _graphic.setPosition(location);
        _location.copy(location);
        if (SpsConfig.get().entityGridEnabled) {
            EntityManager.get().updateGridLocation(this, oldLocation);
        }
    }

    public void setFacingLeft(boolean value) {
        facingLeft = value;
        _graphic.flip(!facingLeft, false);
    }

    public boolean move(float amountX, float amountY) {
        return move(amountX, amountY, false);
    }

    public boolean move(float amountX, float amountY, boolean normalizeToSpriteDimensions) {
        if (normalizeToSpriteDimensions) {
            amountX = normalizeDistance(amountX);
            amountY = normalizeDistance(amountY);
        }
        target.reset(_location.X + amountX, _location.Y + amountY, false);
        if (amountX > 0) {
            setFacingLeft(false);
        }
        if (amountX < 0) {
            setFacingLeft(true);
        }
        if (!CoordVerifier.isValidX(target)) {
            if (target.X > Screen.pos(50, 0).X) {
                target.reset(Renderer.get().VirtualWidth - SpsConfig.get().spriteWidth, target.Y, false);
            }
            else {
                target.reset(0, target.Y, false);
            }
        }
        if (!CoordVerifier.isValidY(target)) {
            if (target.Y > Screen.pos(0, 50).Y) {
                target.reset(target.X, Renderer.get().VirtualHeight - SpsConfig.get().spriteHeight, false);
            }
            else {
                target.reset(target.X, 0, false);
            }

        }
        if (target.X != 0 || target.Y != 0) {
            updateLocation(target);
            return true;
        }
        return false;
    }

    public boolean isActive() {
        return _isActive;
    }

    public void setInactive() {
        _isActive = false;
    }

    public boolean isBlocking() {
        if (_isBlocking == null) {
            return false;
        }
        return _isBlocking;
    }

    public Point2 getLocation() {
        return _location;
    }

    public void setLocation(Point2 location) {
        _graphic.setPosition(location);
        _location.copy(location);
    }

    public boolean isGraphicLoaded() {
        return (_graphic != null);
    }

    protected void setSpriteInfo(SpriteInfo sprite) {
        _graphic.setSpriteInfo(sprite);
    }

    public DrawDepth getDepth() {
        return _graphic.getDepth();
    }

    public boolean contains(Point2 target) {
        return target.GridX == getLocation().GridX && target.GridY == getLocation().GridY;
    }

    public void setInteraction(boolean isInteracting) {
        _isInteracting = isInteracting;
    }

    public boolean isInteracting() {
        return _isInteracting;
    }

    public void setInteracting(boolean isInteracting) {
        _isInteracting = isInteracting;
    }

    public EntityType getEntityType() {
        return _entityType;
    }

    @Override
    public int compareTo(Object o) {
        Entity e = (Entity) o;
        return getDepth().DrawDepth - e.getDepth().DrawDepth;
    }

    public void recalculateEdge() {
        if (_graphic.hasDynamicEdges()) {
            _graphic.setEdge(SpriteEdge.determine(_entityType, _location));
        }
    }
}
