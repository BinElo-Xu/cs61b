import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testOffByOne() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('r', 'q'));
        assertFalse(offByOne.equalChars('a', 'e'));
        assertFalse(offByOne.equalChars('f', 'd'));
    }
    @Test
    public void testisPalindrome() {
        OffByOne p = new OffByOne();
        assertFalse(p.isPalindrome("noon"));
        assertFalse(p.isPalindrome("racecar"));
        assertTrue(p.isPalindrome(""));
        assertTrue(p.isPalindrome("a"));
        assertTrue(p.isPalindrome("ab"));
        assertFalse(p.isPalindrome("aa"));
        assertTrue(p.isPalindrome("aab"));
        assertTrue(p.isPalindrome("flake"));
   }
}
