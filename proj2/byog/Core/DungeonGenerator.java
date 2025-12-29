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
 * https://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/
 */

public class DungeonGenerator implements Serializable {
    // The world of Dungeon.
    TETile[][] world;
    private int width;
    private int height;
    // The number of attempt to generate room.
    private int numberRoomTries = 30;
    // The max size of one room, must be set to less than stage size.
    private int roomMaxSize = 3;
    // Rooms that in the dungeon.
    private List<Room> rooms;
    private Set<Room> deadRooms;
    // The index of connection region to which a position belongs.
    private int[][] region;
    // Index of current region being carved(雕刻).
    private int currentRegion;
    private Random random;
    // Position where the player born.
    Position playerBorn;
    private Player player;
    //The percentage of change direction.
    private static final int WINDING_PERCENT = 50;
    //The chance of extra connection to connect room and road.
    private static final int EXTRA_CONNECT_CHANCE = 3;

    public DungeonGenerator(int numberRoomTries, int roomMaxSize, Random random, int width, int height) {
        this.numberRoomTries = numberRoomTries;
        this.roomMaxSize = roomMaxSize;
        this.random = random;
        this.width = width;
        this.height = height;
        world = new TETile[width][height];
        generate();
        player = new Player(world, playerBorn);
    }

    // Move the player in the map.
    public void movePlayer(Toward toward) {
        player.move(world, toward);
    }

    //Return if player win.
    public boolean playerWin() {
        return player.playerWin;
    }

    /**
     * Generate the Dungeon.
     */
    private void generate() {
        rooms = new ArrayList<>();
        currentRegion = -1;
        if (height % 2 == 0 || width % 2 == 0) {
            throw new IllegalArgumentException("Stage must be odd-size");
        }
        fillWithWall();
        region = new int[width][height];
        addRoom();
        //finish the grow maze method using growing tree algorithm.
        for (int k = 1; k < width; k += 2) {
            for (int l = 1; l < height; l += 2) {
                Position pos = new Position(k, l);
                if (getTile(pos) != Tileset.WALL) {
                    continue;
                }
                growMaze(pos);
            }
        }
        connectRegions();
        removeDeadEnds();
        for (Room room : rooms) {
            removeDeadRooms(room);
        }
        rooms.removeAll(deadRooms);
        addWayOut();
    }

    // Return the description of position (x, y).
    public String descriptionPos(Position pos) {
        if (pos.x < 0 || pos.x >= width || pos.y < 0 || pos.y >= height) {
            return "Nothing is here";
        }
        TETile tile = getTile(pos);
        if (tile == Tileset.WALL) {
            return "Wall";
        } else if (tile == Tileset.UNLOCKED_DOOR) {
            return "Unlocked door";
        } else if (tile == Tileset.PLAYER) {
           return "Player";
        } else {
           return "Floor";
        }
    }

    /**
     * Add location random room to the stage.
     */
    private void addRoom() {
        for (int i = 0; i < numberRoomTries; i += 1) {
            int size = RandomUtils.uniform(random, 1, 3 + roomMaxSize) * 2 + 1;
            // Let the room not just square.
            int rectangularity = RandomUtils.uniform(random, 0, 1 + size / 2) * 2;
            int roomWidth = size;
            int roomHeight = size;
            if (RandomUtils.bernoulli(random)) {
                roomWidth += rectangularity;
            } else {
                roomHeight += rectangularity;
            }
            int x = RandomUtils.uniform(random,  (this.width - roomWidth) / 2) * 2 + 1;
            int y = RandomUtils.uniform(random, (this.height - roomHeight) / 2) * 2 + 1;
            Room room = new Room(x, y, roomWidth, roomHeight);
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
        for (int i = 1; i < this.width - 1; i += 1) {
            for (int j = 1; j < this.height - 1; j += 1) {
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
            for (int i = 1; i < this.width - 1; i += 1) {
                for (int j = 1; j < this.height - 1; j += 1) {
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
     * Remove the closed rooms.
     * @param room: The room which need to check.
     */
    private void removeDeadRooms(Room room) {
        deadRooms = new HashSet<>();
        boolean isDeadRoom = true;
        int x = room.x - 1;
        int y = room.y - 1;
        for (int i = x; i <= x + room.width + 1; i += 1) {
            if (world[i][y] == Tileset.FLOOR || world[i][y + room.height + 1] == Tileset.FLOOR) {
                isDeadRoom = false;
            }
        }
        for (int j = y; j <= y + room.height + 1; j += 1) {
            if (world[x][j] == Tileset.FLOOR || world[x + room.width + 1][j] == Tileset.FLOOR) {
                isDeadRoom = false;
            }
        }
        if (isDeadRoom) {
            for (int i = room.x; i < room.x + room.width; i += 1) {
                for (int j = room.y; j <= room.y + room.height; j += 1) {
                    world[i][j] = Tileset.WALL;
                    deadRooms.add(room);
                }
            }
        }
    }

    /**
     * Add the way out.
     */
    private void addWayOut() {
        int index = random.nextInt(rooms.size());
        int indexPlayer = random.nextInt(rooms.size());
        Room outRoom = rooms.get(index);
        Room playerRoom = rooms.get(indexPlayer);
        Position pos = new Position(RandomUtils.uniform(random, outRoom.x, outRoom.x + outRoom.width),
                RandomUtils.uniform(random, outRoom.y, outRoom.y + outRoom.height));
        playerBorn = new Position(RandomUtils.uniform(random, playerRoom.x, playerRoom.x + playerRoom.width),
                RandomUtils.uniform(random, playerRoom.y, playerRoom.y + playerRoom.height));
        setTile(pos, Tileset.UNLOCKED_DOOR);
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
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.world[x][y] = Tileset.WALL;
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
     * Check if the position pos beyond world.
     * @param pos: The position where need to check.
     * @return If the pos beyond world.
     */
    private boolean worldContain(Position pos) {
        return pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height;
    }

    /**
     * Gets weather or not an opening can be carved from the given starting
     * [cells] at [pos] to the adjacent cell facing [direction]. Return 'ture'
     * if the starting cell is in bounds and the destination cell is filled.
     */
    private boolean canCarve(Position pos, Direction dir) {
        //Finish the logic of 'canCarve' function, and figure out its principle(原理).
        if (!worldContain(pos.add(dir.position.multiply(3)))) {
            return false;
        }
        return getTile(pos.add(dir.position.multiply(2))) == Tileset.WALL;
    }

    /**
     * Get the tile from position pos.
     */
    private TETile getTile(Position pos) {
        return this.world[pos.x][pos.y];
    }
    /**
     * Set the tile of position pos.
     */
    private void setTile(Position pos, TETile tile) {
        this.world[pos.x][pos.y] = tile;
    }
}
