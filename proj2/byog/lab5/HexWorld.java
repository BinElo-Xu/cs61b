package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.lab5.Position;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final long SEED = 2873124;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Return the width of hth row.
     * @param s: the number of side.
     * @param h: the number of row.
     * @return
     */
    public static int hexRowWidth(int s, int h) {
        int n = h;
        if (n >= s) {
            n = 2 * s - 1 - n;
        }
        return s + 2 * n;
    }

    /**
     * Return the offset of hth row for position p.
     * @param s: the number of side.
     * @param h: the number of row.
     * @return
     */
    public static int hexRowOffset(int s, int h) {
        int offSet = h;
        if (h >= s) {
            offSet = 2 * s - 1 - offSet;
        }
        return -offSet;
    }

    /**
     * Draw a row of block start from position p.
     * @param world: The map.
     * @param p: Start position p.
     * @param n: How many blocks need to draw.
     * @param t: The Type of blocks.
     */
    public static void addRow(TETile[][] world, Position p, int n, TETile t) {
        for (int x = 0; x < n; x++) {
            int xCoord = p.x + x;
            int yCoord = p.y;
            world[xCoord][yCoord] = t;
        }
    }

    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least 2");
        }
        for (int i = 0; i < 2 * s; i += 1) {
            int offSet = hexRowOffset(s, i);
            int num = hexRowWidth(s, i);
            int startX = p.x + offSet;
            int startY = p.y + i;
            Position startPos = new Position(startX, startY);
            addRow(world, startPos, num, t);
        }
    }

    public static Position getNextColumnHexagonPos(Position nowPos) {
        return new Position(nowPos.x, nowPos.y - 6);
    }

    public static Position getNextRowHexagonPos(Position nowPos, int column) {
        Position rowPos = new Position(nowPos.x + 5, nowPos.y + 3);
        if (column > 2) {
            rowPos = new Position(nowPos.x + 5, nowPos.y - 3);
        }
        return rowPos;
    }

    public static int getColumnNumber(int i) {
        int ans = i;
        if (i > 2) {
            ans = 4 - i;
        }
        return ans;
    }
    public static int getNumberOfHexagons(int column) {
        int ans = 3 + column;
        if (column > 2) {
            ans = 5 - column;
        }
        return ans;
    }
    public static void drawColumn(TETile[][] world, Position startPos, TETile t, int column) {
        int number = getNumberOfHexagons(column);
        Position pos = new Position(startPos.x, startPos.y);
        for (int x = 0; x < number; x++) {
            addHexagon(world, pos, 3, t);
            pos = getNextColumnHexagonPos(pos);
        }
    }

    public static void drawMap(TETile[][] world) {
        Position Pos = new Position(2, 24);
        for (int x = 0; x < 5; x += 1) {
            int column = getColumnNumber(x);
            TETile t = randomTile();
            drawColumn(world, Pos, t, column);
            Pos = getNextRowHexagonPos(Pos, x + 1);
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            default: return Tileset.FLOOR;
        }
    }

    public static void main(String[] args) {
        int WIDTH = 50;
        int HEIGHT = 50;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        drawMap(world);

        ter.renderFrame(world);
    }

}
