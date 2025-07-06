import org.w3c.dom.Node;

public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> ans = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            ans.addLast(ch);
        }
        return ans;
    }

    public boolean isPalindrome(String word) {
        return helpIsPalindrome(word, 0, word.length() - 1);
    }

    private boolean helpIsPalindrome(String word, int start, int end) {
        if (end - start < 1) {
            return true;
        }
        if (word.charAt(start) != word.charAt(end)) {
            return false;
        }
        return helpIsPalindrome(word, start + 1, end - 1);
    }
}
