package byog.Core;

import byog.TileEngine.TETile;

public class Stage {
    TETile[][] world;
    int width;
    int height;
    public Stage(int width, int height) {
        this.world = new TETile[width][height];
        this.width = width;
        this.height = height;
    }

    /**
     * Return if the position pos in the stage.
     */
    public boolean contain(Position pos) {
        return pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height;
    }

}
