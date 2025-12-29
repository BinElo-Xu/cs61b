package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Player implements Serializable {
    transient TETile icon = Tileset.PLAYER;
    Position pos;
    boolean playerWin = false;

    public Player(TETile[][] world, Position pos) {
        this.pos = pos;
        world[pos.x][pos.y] = icon;
    }

    public void move(TETile[][] world, Toward direction) {
        Position newPosition = pos.add(direction.position);
        if (world[newPosition.x][newPosition.y] == Tileset.FLOOR) {
            world[newPosition.x][newPosition.y] = icon;
            world[pos.x][pos.y] = Tileset.FLOOR;
            pos = newPosition;
        }
        if (world[newPosition.x][newPosition.y] == Tileset.UNLOCKED_DOOR) {
            playerWin = true;
        }
    }
}
