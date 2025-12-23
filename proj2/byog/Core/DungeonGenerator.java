package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Copy from "https://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/"
 */

public class DungeonGenerator implements Serializable {
    // The stage of Dungeon.
    Stage stage;
    // The number of attempt to generate room.
    private int numberRoomTries = 30;
    // The max size of one room, must be set to less than stage size.
    private int roomMaxSize = 3;
    // Rooms that in the dungeon.
    private List<Room> rooms;
    // The index of connection region to which a position belongs.
    private int[][] region;
    // Index of current region being carved(雕刻).
    private int currentRegion;
    private Random random;
    //The percentage of change direction.
    private static final int WINDING_PERCENT = 50;
    //The chance of extra connection to connect room and road.
    private static final int EXTRA_CONNECT_CHANCE = 5;

    public void initialize(Stage stage, int numberRoomTries, int roomMaxSize) {
        this.stage = stage;
        this.numberRoomTries = numberRoomTries;
        this.roomMaxSize = roomMaxSize;
    }

    /**
     * Generate the Dungeon.
     */
    public void generate(long seed) {
        rooms = new ArrayList<>();
        currentRegion = -1;
        random = new Random(seed);
        if (stage.height % 2 == 0 || stage.width % 2 == 0) {
            throw new IllegalArgumentException("Stage must be odd-size");
        }
        fillWithWall();
        region = new int[stage.width][stage.height];
        addRoom();
        //finish the grow maze method using growing tree algorithm.
        for (int k = 1; k < stage.width; k += 2) {
            for (int l = 1; l < stage.height; l += 2) {
                Position pos = new Position(k, l);
                if (getTile(pos) != Tileset.WALL) {
                    continue;
                }
                growMaze(pos);
            }
        }
        connectRegions();
        removeDeadEnds();
        addWayOut();
    }

    /**
     * Set the RANDOM_SEED.
     */
    public void setRandomSeed(long seed) {
        //Random seed
        random = new Random(seed);
    }

    /**
     * Add location random room to the stage.
     */
    private void addRoom() {
        for (int i = 0; i < numberRoomTries; i += 1) {
            int size = RandomUtils.uniform(random, 1, 3 + roomMaxSize) * 2 + 1;
            // Let the room not just square.
            int rectangularity = RandomUtils.uniform(random, 0, 1 + size / 2) * 2;
            int width = size;
            int height = size;
            if (RandomUtils.bernoulli(random)) {
                width += rectangularity;
            } else {
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
            for (int k = room.x; k < room.x + room.width; k += 1) {
                for (int h = room.y; h < room.y + room.height; h += 1) {
                    Position pos = new Position(k, h);
                    carve(pos, Tileset.FLOOR);
                }
            }
        }
    }

    /**
     * Grow maze from the empty place that do not be placed by room.
     */
    private void growMaze(Position start) {
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
                if (unmadeCells.contains(lastDir) && random.nextInt(100) > WINDING_PERCENT) {
                    dir = lastDir;
                } else {
                    //Take out a random item from unmadeCells.
                    int index = random.nextInt(unmadeCells.size());
                    dir = unmadeCells.get(index);
                }
                carve(cell.add(dir.position), Tileset.FLOOR);
                carve(cell.add(dir.position.multiply(2)), Tileset.FLOOR);

                cells.add(cell.add(dir.position.multiply(2)));
                lastDir = dir;
            } else {
                cells.remove(cells.size() - 1);
                lastDir = null;
            }
        }
    }

    /**
     * Find all the tiles that can connect two (or more) regions.
     */
    private void connectRegions() {
        Map<Position, Set<Integer>> connectorRegions = new HashMap<>();
        for (int i = 1; i < stage.width - 1; i += 1) {
            for (int j = 1; j < stage.height - 1; j += 1) {
                Position pos = new Position(i, j);
                if (getTile(pos) != Tileset.WALL) {
                    continue;
                }

                Set<Integer> regions = new HashSet<>();
                for (Direction dir : Direction.CARDINAL) {
                    Integer region = getRegion(pos.add(dir.position));
                    if (region != null) {
                        regions.add(region);
                    }
                }
                if (regions.size() < 2) {
                    continue;
                }
                connectorRegions.put(pos, regions);
            }
        }
        // Hash map can not ensure the sorts of one list, so we need to sort it again to
        // ensure the sorts of list.
        List<Position> connectors = new ArrayList<>(connectorRegions.keySet());
        connectors.sort(Comparator.comparingInt((Position p) -> p.x).thenComparingInt(p -> p.y));

        /*
        Keep track of which region has been merged. This maps an original region index
        to the one it has been merged to.
         */
        Map<Integer, Integer> merged = new HashMap<>();
        Set<Integer> openRegions = new HashSet<>();
        for (int i = 0; i < currentRegion; i += 1) {
            merged.put(i, i);
            openRegions.add(i);
        }

        //Keep connecting regions until we're down to one.
        while (openRegions.size() > 1) {
            int index = random.nextInt(connectors.size());
            Position connector = connectors.get(index);
            //Carve the connection.
            setTile(connector, Tileset.FLOOR);
            Set<Integer> regions = connectorRegions.get(connector).stream().map(
                    merged::get).collect(Collectors.toSet());
            Integer dest = regions.iterator().next();
            List<Integer> sources = regions.stream().skip(1).collect(Collectors.toList());
            /*
            Merge all the affected regions. We hava to look at "all" of the  regions because
            other region may have previously been merged with some of the ones we're merging now.
             */
            for (int i = 0; i < currentRegion; i += 1) {
                if (sources.contains(merged.get(i))) {
                    merged.put(i, dest);
                }
            }
            //Sources are no longer in use.
            openRegions.removeAll(sources);
            //Remove any connectors that aren't needed anymore.
            connectors.removeIf((pos) -> {
                if (connector.sub(pos) < 2) {
                    return true;
                }
                Set<Integer> area = connectorRegions.get(pos).stream().map(
                        merged::get).collect(Collectors.toSet());
                if (area.size() > 1) {
                    return false;
                }
                //This connector is not needed, but connect is occasionally so that the dungeon isn't singly-connected.
                if (random.nextInt(100) < EXTRA_CONNECT_CHANCE) {
                    carve(pos, Tileset.FLOOR);
                }
                return true;
            });
        }
    }

    /**
     * Remove dead ends.
     */
    private void removeDeadEnds() {
        boolean done = false;

        while (!done) {
            done = true;
            for (int i = 1; i < stage.width - 1; i += 1) {
                for (int j = 1; j < stage.height - 1; j += 1) {
                    Position pos = new Position(i, j);
                    if (getTile(pos) == Tileset.WALL) {
                        continue;
                    }

                    int exists = 0;
                    for (Direction dir : Direction.CARDINAL) {
                        if (getTile(pos.add(dir.position)) != Tileset.WALL) {
                            exists++;
                        }
                    }
                    if (exists != 1) {
                        continue;
                    }
                    done = false;
                    setTile(pos, Tileset.WALL);
                }
            }
        }

    }

    /**
     * Add the way out.
     */
    private void addWayOut() {
        boolean flag = false;
        while (!flag) {
            flag = true;
            int x = random.nextInt(stage.width);
            int y = random.nextInt(stage.height);
            Position pos = new Position(x, y);
            if (getTile(pos) == Tileset.WALL) {
                flag = false;
                continue;
            }
            setTile(pos, Tileset.LOCKED_DOOR);
        }
    }

    /**
     * Return the region of position pos.
     */
    private Integer getRegion(Position pos) {
        return region[pos.x][pos.y];
    }

    /**
     * Fill all the world with wall.
     */
    public void fillWithWall() {
        for (int x = 0; x < stage.width; x++) {
            for (int y = 0; y < stage.height; y++) {
                stage.world[x][y] = Tileset.WALL;
            }
        }
    }

    /**
     * Update the index of connected region to which current room belongs.
     */
    private void startRegion() {
        currentRegion += 1;
    }

    /**
     * Carve the position pos.
     */
    private void carve(Position pos, TETile tile) {
        setTile(pos, tile);
        region[pos.x][pos.y] = currentRegion;
    }

    /**
     * Gets weather or not an opening can be carved from the given starting
     * [cells] at [pos] to the adjacent cell facing [direction]. Return 'ture'
     * if the starting cell is in bounds and the destination cell is filled.
     */
    private boolean canCarve(Position pos, Direction dir) {
        //Finish the logic of 'canCarve' function, and figure out its principle(原理).
        if (!stage.contain(pos.add(dir.position.multiply(3)))) {
            return false;
        }
        return getTile(pos.add(dir.position.multiply(2))) == Tileset.WALL;
    }

    /**
     * Get the tile from position pos.
     */
    private TETile getTile(Position pos) {
        return stage.world[pos.x][pos.y];
    }
    /**
     * Set the tile of position pos.
     */
    private void setTile(Position pos, TETile tile) {
        stage.world[pos.x][pos.y] = tile;
    }
}
