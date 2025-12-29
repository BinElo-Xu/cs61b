package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;


public class Game {
    /* Feel free to change the width and height. */
    TERenderer ter = new TERenderer();
    public static final int WIDTH = 81;
    public static final int HEIGHT = 51;
    private static final int UI_WIDTH = WIDTH;
    private static final int UI_HEIGHT = HEIGHT + 2;
    private String option = "";
    private String command = "";
    private long seed = -1;
    private Random rand;
    private boolean gameOver = false;
    private boolean needLoad = false;
    private DungeonGenerator generator;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        gameOver = false;
        ter.initialize(UI_WIDTH, UI_HEIGHT);
        showMainMenu();
        analyzeCommand(getKeyboardInput());
        if (needLoad) {
            load();
        } else {
            rand  = new Random(seed);
            generator = new DungeonGenerator(30, 3, rand, WIDTH, HEIGHT);
        }
        play(generator.world);
    }

    //Load the game from a .txt file.
    private void load() {
        File f = new File("./world.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                DungeonGenerator game = (DungeonGenerator) os.readObject();
                os.close();
                this.generator = game;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
    }

    //The main logic of playing game.
    private void play(TETile[][] worldState) {
        while (!gameOver) {
            ter.renderFrame(worldState);
            refreshGameUi();
            refreshWorldState();
        }
        StdDraw.clear();
        StdDraw.show();
        System.exit(0);
    }

    //Refresh game's UI, especial the cursor point and player information.
    private void refreshGameUi() {
        StdDraw.setPenColor(StdDraw.WHITE);
        Font bigFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(bigFont);
        StdDraw.text(UI_WIDTH / 2, UI_HEIGHT - 1, cursorPointDescription());
        StdDraw.line(0, UI_HEIGHT - 2, UI_WIDTH, UI_HEIGHT - 2);
        StdDraw.setFont();
        StdDraw.show();
    }

    //Return the cursor point's description.
    private String cursorPointDescription() {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();

        int posX = (int) x;
        int posY = (int) y;

        return generator.descriptionPos(new Position(posX, posY));
    }

    //Refresh the whole world.
    private void refreshWorldState() {
        generator.movePlayer(moveWithKeyboard());
        if (generator.playerWin()) {
            quitGame();
        }
    }

    //Move the player through keyboard.
    private Toward moveWithKeyboard() {
        char c;
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                return Toward.STAY;
            }
            c = StdDraw.nextKeyTyped();
            switch (c) {
                case 'w':
                case 'W': {
                    return Toward.w;
                }
                case 's':
                case 'S': {
                    return Toward.s;
                }
                case 'a':
                case 'A': {
                    return Toward.a;
                }
                case 'd':
                case 'D': {
                    return Toward.d;
                }
                case ':': {
                    while (true) {
                        if (!StdDraw.hasNextKeyTyped()) {
                            continue;
                        }
                        char c2 = StdDraw.nextKeyTyped();
                        if (c2 == 'q' || c2 == 'Q') {
                            quitAndSaveGame();
                            break;
                        }
                    }
                    break;

                }
                default: {
                    return Toward.STAY;
                }
            }
        }
    }

    //Show the main menu at the game beginning.
    private void showMainMenu() {
        int midHeight = UI_HEIGHT / 2;
        int midWidth = UI_WIDTH / 2;

        StdDraw.clear(Color.black);
        Font bigFont = new Font("Monaco", Font.BOLD, 60);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(midWidth, midHeight + 7, "CS61B: THE GAME");
        Font smallFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(smallFont);
        StdDraw.text(midWidth, midHeight + 4, "N: New Game");
        StdDraw.text(midWidth, midHeight + 2, "L: Load Game");
        StdDraw.text(midWidth, midHeight, "Q: Quit");
        StdDraw.setFont();

        StdDraw.show();
    }

    // Just quit the game.
    private void quitGame() {
        gameOver = true;
    }

    // Quit and save the game.
    private void quitAndSaveGame() {
        gameOver = true;
        saveGame(generator);
    }

    //Save the game.
    private void saveGame(DungeonGenerator generator) {
        File f = new File("./world.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(generator);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    // Get the input string from keyboard, and get the value of option and command.
    private String getKeyboardInput() {
        String command = "";
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char c = StdDraw.nextKeyTyped();
            command += String.valueOf(c);

            if (c == 's' || c == 'S') {
                break;
            }
            if (c == 'l' || c == 'L') {
                gameOver = false;
                needLoad = true;
                break;
            }
            if (c == 'q' || c == 'Q') {
                break;
            }
        }
        return command;
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
        analyzeCommand(input);
        if (needLoad) {
            load();
        } else {
            rand = new Random(seed);
            generator = new DungeonGenerator(30, 3, rand, WIDTH, HEIGHT);
        }
        evalCommand(command);
        return generator.world;
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
                    generator.movePlayer(Toward.w);
                    break;
                }
                case 's': {
                    generator.movePlayer(Toward.s);
                    break;
                }
                case 'a': {
                    generator.movePlayer(Toward.a);
                    break;
                }
                case 'd': {
                    generator.movePlayer(Toward.d);
                    break;
                }
                case ':':
                case 'q': {
                    quitAndSaveGame();
                    break;
                }
                default: {
                    generator.movePlayer(Toward.STAY);
                    break;
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
            needLoad = true;
            for (int i = 1; i < inputStr.length(); i += 1) {
                command += inputStr.charAt(i);
            }
        } else if (option.equals("q")) {
            quitGame();
        }
    }
    private boolean isNum(char c) {
        return c >= '0' && c <= '9';
    }
}
