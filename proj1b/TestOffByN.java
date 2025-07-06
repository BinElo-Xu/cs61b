import org.junit.Test;
import static org.junit.Assert.*;


public class TestOffByN {
    private static OffByN offByN1 = new OffByN(1);
    private static OffByN offByN2 = new OffByN(2);
    @Test
    public void testOffByN() {
        assertTrue(offByN1.equalChars('a', 'b'));
        assertTrue(offByN1.equalChars('f', 'e'));
        assertTrue(offByN2.equalChars('f', 'h'));
        assertTrue(offByN2.equalChars('a', 'c'));
        assertFalse(offByN1.equalChars('f', 'h'));
        assertFalse(offByN1.equalChars('a', 'c'));
        assertFalse(offByN2.equalChars('f', 'g'));
        assertFalse(offByN2.equalChars('a', 'b'));
    }
}
