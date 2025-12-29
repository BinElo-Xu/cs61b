package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import org.junit.Assert;

import java.util.Random;

public class Test {
    @org.junit.Test
    public void testGenerator() {
        int width = 81;
        int height = 51;
        int testNum = 300;
        while (testNum-- > 0) {
            Random random1 = new Random(1);
            Random random2 = new Random(1);
            DungeonGenerator g1 = new DungeonGenerator(30, 1, random1, width, height);
            DungeonGenerator g2 = new DungeonGenerator(30, 1, random2, width, height);
            Assert.assertEquals(g1.world, g2.world);
        }
        testNum = 300;
        while (testNum-- > 0) {
            Random random1 = new Random();
            Random random2 = new Random();
            DungeonGenerator g1 = new DungeonGenerator(30, 1, random1, width, height);
            DungeonGenerator g2 = new DungeonGenerator(30, 1, random2, width, height);
            Assert.assertNotEquals(g1.world, g2.world);
        }
    }

    @org.junit.Test
    public void testPlayerMove() {
        Random random = new Random(1);
        Game g1 = new Game();
        Game g2 = new Game();
        TETile[][] w1 = g1.playWithInputString("N123SWWSDAAS");
        TETile[][] w2 = g2.playWithInputString("N123SWWSDAAS");
        Assert.assertEquals(w1, w2);
        int times = 100;
        while (times-- > 0) {
            String command1 = "N123S";
            String command2 = "N123S";
            command1 += generateString(random);
            command2 += generateString(random);

            TETile[][] w3 = g1.playWithInputString(command1);
            TETile[][] w4 = g2.playWithInputString(command2);
            Assert.assertNotEquals(w3, w4);
        }
    }

    @org.junit.Test
    public void testSaveGame() {
        Game g1 = new Game();
        TETile[][] w1 = g1.playWithInputString("N123SDDDWWWDDD");
        g1.playWithInputString("N123SDDD:Q");
        TETile[][] w2 = g1.playWithInputString("LWWWDDD");
        Assert.assertEquals(w1, w2);
        g1.playWithInputString("N123SDDD:Q");
        g1.playWithInputString("LWWW:Q");
        TETile[][] w3 = g1.playWithInputString("LDDD:Q");
        Assert.assertEquals(w1, w3);
        g1.playWithInputString("N123SDDD:Q");
        g1.playWithInputString("L:Q");
        g1.playWithInputString("L:Q");
        TETile[][] w4 = g1.playWithInputString("LWWWDDD");
        Assert.assertEquals(w1, w4);
    }

    private String generateString(Random random) {
        char[] chars = {'W', 'S', 'A', 'D'};
        String ans = "";
        int len = random.nextInt(chars.length);
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(chars.length);
            ans += chars[index];
        }
        return ans;
    }

    public static void main(String[] args) {
        Random random = new Random(1);
        DungeonGenerator g1 = new DungeonGenerator(30, 3, random, 81, 51);
        TERenderer ter = new TERenderer();
        ter.initialize(81, 52);
        ter.renderFrame(g1.world);
    }

}
