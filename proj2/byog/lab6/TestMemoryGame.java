package byog.lab6;

import edu.princeton.cs.algs4.StdDraw;
import org.junit.Assert;
import org.junit.Test;

public class TestMemoryGame {

    @org.junit.Test
    public void testGenerateRandomString() {
        MemoryGame gameSame1 = new MemoryGame( 1);
        MemoryGame gameSame2 = new MemoryGame( 1);
        MemoryGame gameDiff1 = new MemoryGame(3);
        MemoryGame gameDiff2 = new MemoryGame( 4);
        String pre = "";
        String real = "";
        for (int i = 0; i < 100; i += 1) {
            real = gameSame1.generateRandomString(5);
            pre = gameSame2.generateRandomString(5);
            Assert.assertEquals(pre, real);
            real = gameDiff1.generateRandomString(5);
            pre = gameDiff2.generateRandomString(5);
            Assert.assertNotEquals(pre, real);
        }
    }

    public static void main(String[] args) {
        MemoryGame game = new MemoryGame(40, 40, 3);
        String inputString = game.solicitNCharsInput(5);
        game.drawFrame(inputString);
    }
}
