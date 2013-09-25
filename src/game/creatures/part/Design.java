package game.creatures.part;

public interface Design {
    public static final int BaseColor = -1;
    public static final int Black = 1;
    public static final int White = 2;
    public static final int Empty = 0;

    public int[][] create(int width, int height);
}
