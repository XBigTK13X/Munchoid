package game.creatures.part;

public class RectangleBody implements Design {
    @Override
    public int[][] create(int width, int height) {
        return Common.rectangle(width, height);
    }
}
