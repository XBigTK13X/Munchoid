package game.creatures;

import sps.core.RNG;

import java.util.ArrayList;
import java.util.List;

public class Body {
    private List<BodyPart> parts;

    private Creature _owner;

    public Body(Creature owner, int numberOfParts, int partWidthMin, int partHeightMin, int partWidthMax, int partHeightMax) {
        _owner = owner;
        parts = new ArrayList<BodyPart>();
        for (int ii = 0; ii < numberOfParts; ii++) {
            parts.add(new BodyPart(RNG.next(partWidthMin, partWidthMax), RNG.next(partHeightMin, partHeightMax), this));
        }
    }

    public void draw() {
        for (BodyPart part : parts) {
            part.draw();
        }
    }

    public BodyPart getRandomPart() {
        return parts.get(RNG.next(0, parts.size()));
    }

    public void update() {
        for (int ii = 0; ii < parts.size(); ii++) {
            if (!parts.get(ii).isAlive()) {
                parts.remove(ii);
            }
        }
    }

    public boolean isAlive() {
        return parts.size() > 0;
    }

    public Creature getOwner() {
        return _owner;
    }
}
