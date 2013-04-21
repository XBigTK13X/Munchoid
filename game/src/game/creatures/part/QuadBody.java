package game.creatures.part;

import game.creatures.Atom;

public class QuadBody implements Design{
    @Override
    public boolean[][] create(int width, int height) {
        return Common.rectangle(width,height);
    }
}
