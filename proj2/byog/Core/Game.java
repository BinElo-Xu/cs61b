package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;


public class Game {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 81;
    public static final int HEIGHT = 51;
    private String option = "";
    private String command = "";
    private long seed = -1;

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
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        DungeonGenerator generator = new DungeonGenerator();
        Stage stage = new Stage(WIDTH, HEIGHT);
        analyzeCommand(input);
        switch (option) {
            case "n": {
                generator.initialize(stage, 20, 3);
                generator.generate(seed);
                evalCommand(command);
                break;
            }
            case "l": {
                // The logic of load the game.
                evalCommand(command);
                break;
            }
            case "q": {
                break;
            }
            default: { }
        }
        return generator.stage.world;
    }


    /**
     * Evaluate the command based the game rules.
     * @param command:The command user input.
     */
    private void evalCommand(String command) {
        char[] chars = command.toCharArray();
        for (char c : chars) {
            switch (c) {
                case 'w': {

                }
                case 's': {

                }
                case 'a': {

                }
                case 'd': {

                }
                case ':':
                case 'q': {

                }
            }
        }
    }

    /**
     *Analyze the input str, divide it to "option", "command" and "seed".
      */
    private void analyzeCommand(String input) {
        String inputStr = input.toLowerCase();
        option = String.valueOf(inputStr.charAt(0));
        if (option.equals("n")) {
            String seedStr = "";
            int index = 1;
            while (isNum(inputStr.charAt(index))) {
                seedStr += inputStr.charAt(index);
                index += 1;
            }
            seed = Long.parseLong(seedStr);
            for (; index < inputStr.length(); index += 1) {
                command += inputStr.charAt(index);
            }
        } else if (option.equals("l")) {
            for (int i = 1; i < inputStr.length(); i += 1) {
                command += inputStr.charAt(i);
            }
        }
    }
    private boolean isNum(char c) {
        return c >= '0' && c <= '9';
    }
}
