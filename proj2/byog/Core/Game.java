package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import java.io.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 81;
    public static final int HEIGHT = 51;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        DungeonGenerator generator = new DungeonGenerator();
        if (input.charAt(0) == 'N') {
            Stage stage = new Stage(WIDTH, HEIGHT);
            int i = 1;
            String seed = "";
            while (input.charAt(i) != 'S') {
                seed += input.charAt(i);
                i += 1;
            }
            long SEED = Long.parseLong(seed);
            generator.setRandomSeed(SEED);
            generator.initialize(stage, 30, 3);
            generator.generate();
        }
        else if (input.charAt(0) == 'L') {
            File latestFile = new File("./latest.txt");
            try {
                FileInputStream fileIn = new FileInputStream(latestFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                generator = (DungeonGenerator) in.readObject();
                in.close();
                fileIn.close();
            } catch (Exception e) {
                if (!latestFile.exists()) {
                    System.out.println("File not found");
                }
                e.printStackTrace();
            }
        }
        else if (input.charAt(0) == 'Q') {
            System.exit(0);
        }
        return generator.stage.world;
    }
}
