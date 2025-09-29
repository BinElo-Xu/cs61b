package byog.lab5;

import org.junit.Test;
import static org.junit.Assert.*;

/***
 * The test class of HexWorld.
 */
public class TestHexWorld {
    @Test
    public void testHexRowWidth() {
        assertEquals(3, HexWorld.hexRowWidth(3, 0));
        assertEquals(7, HexWorld.hexRowWidth(3, 2));
        assertEquals(7, HexWorld.hexRowWidth(3, 3));
        assertEquals(5, HexWorld.hexRowWidth(3, 4));
    }
    @Test
    public void testHexRowOffset() {
        assertEquals(0, HexWorld.hexRowOffset(3, 0));
        assertEquals(-1, HexWorld.hexRowOffset(3, 1));
        assertEquals(-2, HexWorld.hexRowOffset(3, 3));
        assertEquals(-1, HexWorld.hexRowOffset(3, 4));
    }
}
