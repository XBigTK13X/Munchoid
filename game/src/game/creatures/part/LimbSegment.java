package game.creatures.part;

public class LimbSegment implements Design {
    @Override
    public int[][] create(int width, int height) {
        return Common.rectangle(Math.min(width, height), Math.min(width, height));
        //return Common.rectangle(width, height / 2);
    }
}
