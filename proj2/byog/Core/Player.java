package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Player implements Serializable {
    TETile icon = Tileset.PLAYER;
    Position pos;
    boolean playerWin = false;

    public Player(TETile[][] world, Position pos) {
        this.pos = pos;
        world[pos.x][pos.y] = icon;
    }

    public void move(TETile[][] world, Toward direction) {
        Position newPosition = pos.add(direction.position);
        /* The Tileset class is static, if we use '==', it will case some error
        in the serializable process.
         */

        if (world[newPosition.x][newPosition.y].description().equals(Tileset.FLOOR.description())) {
            world[newPosition.x][newPosition.y] = icon;
            world[pos.x][pos.y] = Tileset.FLOOR;
            pos = newPosition;
        }
        if (world[newPosition.x][newPosition.y].description().equals(Tileset.UNLOCKED_DOOR.description())) {
            playerWin = true;
        }
    }
}
