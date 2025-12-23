package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }
        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        this.rand = new Random(seed);
    }

    // This constructor just for test.
    public MemoryGame(int seed) {
        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        int index = 0;
        String ans = "";
        for (int i = 0; i < n; i += 1) {
            index = rand.nextInt(CHARACTERS.length);
            ans += CHARACTERS[index];
        }
        return ans;
    }

    public void drawFrame(String s) {
        int midWidth = this.width / 2;
        int midHeight = this.height / 2;
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);
        if (!gameOver) {
            Font font2 = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font2);
            StdDraw.textLeft(1, height - 1, "Round: " + this.round);
            StdDraw.text(midWidth, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.text(midWidth, midHeight, s);

        StdDraw.show();
    }

    public void flashSequence(String letters) {
        int index = 0;
        while (index < letters.length()) {
            drawFrame(letters.substring(index, index + 1));
            StdDraw.pause(750);
            drawFrame("");
            StdDraw.pause(750);
            index += 1;
        }
    }

    public String solicitNCharsInput(int n) {
        String input = "";
        drawFrame(input);
        while (input.length() < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char character = StdDraw.nextKeyTyped();
            input += String.valueOf(character);
            drawFrame(input);
        }
        StdDraw.pause(750);
        return input;
    }

    public void startGame() {
        round = 1;
        gameOver = false;
        playerTurn = false;
        while (!gameOver) {
            playerTurn = false;
            drawFrame("round" + round + " Good Luck!");
            StdDraw.pause(1500);
            String ans = generateRandomString(round);
            flashSequence(ans);
            playerTurn = true;
            String input = solicitNCharsInput(round);
            if (!input.equals(ans)) {
                gameOver = true;
                drawFrame("Game Over! You made it to round:" + round);
            } else {
                round += 1;
                drawFrame("Correct, well done!");
            }
        }
    }

}
