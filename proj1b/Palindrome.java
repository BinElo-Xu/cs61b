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

    public boolean isPalindrome(String word, CharacterComparator cc) {
        return helpIsPalindrome1(word, cc, 0, word.length() - 1);
    }
    private boolean helpIsPalindrome1(String word, CharacterComparator cc, int start, int end) {
        if (end - start < 1) {
            return true;
        }
        if (!cc.equalChars(word.charAt(start), word.charAt(end))) {
            return false;
        }
        return helpIsPalindrome1(word, cc, start + 1, end - 1);
    }
}
