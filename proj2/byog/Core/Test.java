package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class Test {
    public static void main(String[] args) {
        Game game = new Game();
        TETile[][] world = game.playWithInputString("N123456S");
        TERenderer ter = new TERenderer();
        ter.initialize(81, 51);
        ter.renderFrame(world);
    }
}
