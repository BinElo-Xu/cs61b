import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    } //Uncomment this class once you've created your Palindrome class.

    @Test
    public void testisPalindromeOffByOne() {
        //空白单词
        assertTrue(palindrome.isPalindrome("", new OffByOne()));
        // 单个字母
        assertTrue(palindrome.isPalindrome("a", new OffByOne()));
        // 两个字母
        assertTrue(palindrome.isPalindrome("ab", new OffByOne()));
        assertFalse(palindrome.isPalindrome("bb", new OffByOne()));
        // 多个字母
        assertTrue(palindrome.isPalindrome("acb", new OffByOne()));
        assertTrue(palindrome.isPalindrome("egf", new OffByOne()));
        assertFalse(palindrome.isPalindrome("noon", new OffByOne()));
        assertTrue(palindrome.isPalindrome("flake", new OffByOne()));
    }

    @Test
    public void testisPalindromeOffByN() {
        // 空白单词
        assertTrue(palindrome.isPalindrome("", new OffByN(2)));
        assertTrue(palindrome.isPalindrome("", new OffByN(3)));
        // 一个字母
        assertTrue(palindrome.isPalindrome("a", new OffByN(2)));
        assertTrue(palindrome.isPalindrome("b", new OffByN(3)));
        // 两个字母
        assertTrue(palindrome.isPalindrome("ac", new OffByN(2)));
        assertTrue(palindrome.isPalindrome("ad", new OffByN(3)));
        assertFalse(palindrome.isPalindrome("ad", new OffByN(2)));
        assertFalse(palindrome.isPalindrome("ac", new OffByN(3)));
        // 多个字母
        assertTrue(palindrome.isPalindrome("abdc", new OffByN(2)));
        assertTrue(palindrome.isPalindrome("abed", new OffByN(3)));
        assertFalse(palindrome.isPalindrome("abed", new OffByN(2)));
        assertFalse(palindrome.isPalindrome("abdc", new OffByN(3)));
    }
}
