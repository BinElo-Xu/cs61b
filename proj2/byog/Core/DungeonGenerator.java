package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonGenerator {
    // The stage of Dungeon.
    private static Stage stage;
    // The number of attempt to generate room.
    private static int numberRoomTries;
    // The max size of one room, must be set to less than stage size.
    private static int RoomMaxSize = 0;
    // Rooms that in the dungeon.
    private static List<Room> rooms = new ArrayList<Room>();
    // The index of connection region to which a position belongs.
    private static int[][] region;
    // Index of current region being carved(雕刻).
    private static int currentRegion = -1;
    //Random seed
    private static int RANDOM_SEED = 1234;
    private static Random random = new Random(RANDOM_SEED);
    //
    private static final int windingPercent = 50;

    public DungeonGenerator(Stage stage, int numberRoomTries, int RoomMaxSize) {
        DungeonGenerator.stage = stage;
        this.numberRoomTries = numberRoomTries;
        this.RoomMaxSize = RoomMaxSize;
    }

    /**
     * Generate the Dungeon.
     */
    public static void generate() {
        if (stage.height % 2 == 0 || stage.width % 2 == 0) {
            throw new IllegalArgumentException("Stage must be odd-size");
        }
        fillWithWall();
        region = new int[stage.width][stage.height];
        addRoom();
        //TODO: finish the grow maze method using growing tree algorithm.
        for (int k = 1; k < stage.width; k += 2) {
            for (int l = 1; l < stage.height; l += 2) {
                Position pos = new Position(k, l);
                if (getTile(pos) != Tileset.WALL) continue;
                growMaze(pos);
            }
        }
    }

    /**
     * Add location random room to the stage.
     */
    private static void addRoom() {
        for (int i = 0; i < numberRoomTries; i += 1) {
            int size = RandomUtils.uniform(random, 1, 3 + RoomMaxSize) * 2 + 1;
            // Let the room not just square.
            int rectangularity = RandomUtils.uniform(random, 0, 1 + size / 2) * 2;
            int width = size;
            int height = size;
            if (RandomUtils.bernoulli(random)) {
                width += rectangularity;
            }
            else {
                height += rectangularity;
            }
            int x = RandomUtils.uniform(random,  (stage.width - width) / 2) * 2 + 1;
            int y = RandomUtils.uniform(random, (stage.height - height) / 2) * 2 + 1;
            Room room = new Room(x, y, width, height);
            boolean overlaps = false;
            for (Room other : rooms) {
                if (room.isOverlapsTo(other)) {
                    overlaps = true;
                    break;
                }
            }
            if (overlaps) {
                continue;
            }
            rooms.add(room);
            startRegion();
            for (int k = room.x + 1; k < room.x + room.width - 1; k += 1) {
                for (int h = room.y + 1; h < room.y + room.height - 1; h += 1) {
                    Position pos = new Position(k, h);
                    carve(pos, Tileset.FLOOR);
                }
            }
        }
    }

    /**
     * Grow maze from the empty place that do not be placed by room.
     */
    private static void growMaze(Position start) {
        List<Position> cells = new ArrayList<>();
        Direction lastDir = null;

        startRegion();
        carve(start, Tileset.FLOOR);
        cells.add(start);
        while (!cells.isEmpty()) {
            Position cell = cells.get(cells.size() - 1);
            List<Direction> unmadeCells = new ArrayList<>();

            for (Direction dir : Direction.CARDINAL) {
                if (canCarve(cell, dir)) {
                    unmadeCells.add(dir);
                }
            }

            if (!unmadeCells.isEmpty()) {
                Direction dir;
                if (unmadeCells.contains(lastDir) && random.nextInt(100) > windingPercent) {
                    dir = lastDir;
                }
                else {
                    //TODO: take out a random item from unmadeCells.
                    int index = random.nextInt(unmadeCells.size());
                    dir = unmadeCells.get(index);
                }
                carve(cell.add(dir.position), Tileset.FLOOR);
                carve(cell.add(dir.position.multiply(2)), Tileset.FLOOR);

                cells.add(cell.add(dir.position.multiply(2)));
                lastDir = dir;
            }
            else {
                cells.remove(cells.size() - 1);
                lastDir = null;
            }
        }
        return;
    }

    /**
     * Fill all the world with wall.
     */
    public static void fillWithWall() {
        for (int x = 0; x < stage.width; x++) {
            for (int y = 0; y < stage.height; y++) {
                stage.world[x][y] = Tileset.WALL;
            }
        }
    }

    /**
     * Update the index of connected region to which current room belongs.
     */
    private static void startRegion() {
        currentRegion += 1;
    }

    /**
     * Carve the position pos.
     */
    private static void carve(Position pos, TETile tile) {
        setTile(pos, tile);
        region[pos.x][pos.y] = currentRegion;
    }

    /**
     * Gets weather or not an opening can be carved from the given starting
     * [cells] at [pos] to the adjacent cell facing [direction]. Return 'ture'
     * if the starting cell is in bounds and the destination cell is filled.
     */
    private static boolean canCarve(Position pos, Direction dir) {
        //TODO: finish the logic of 'canCarve' function, and figure out its principle(原理).
        if (!stage.contain(pos.add(dir.position.multiply(3)))) {
            return false;
        }
        return getTile(pos.add(dir.position.multiply(2))) == Tileset.WALL;
    }

    /**
     * Get the tile from position pos.
     */
    private static TETile getTile(Position pos) {
        return stage.world[pos.x][pos.y];
    }
    /**
     * Set the tile of position pos.
     */
    private static void setTile(Position pos, TETile tile) {
        stage.world[pos.x][pos.y] = tile;
    }

    public static void main(String[] args) {
        Stage stage = new Stage(81, 51);
        DungeonGenerator generator = new DungeonGenerator(stage, 30000, 3);
        generator.generate();
        TERenderer ter = new TERenderer();
        ter.initialize(81, 51);
        ter.renderFrame(stage.world);
    }
}
