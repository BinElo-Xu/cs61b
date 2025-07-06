public class OffByOne implements CharacterComparator {
    @Override
    public boolean equalChars(char x, char y) {
        int ans = Math.abs(x - y);
        return ans == 1;
    }

    public boolean isPalindrome(String world) {
        return isPalindromehelper(world, 0, world.length() - 1);
    }

    private boolean isPalindromehelper(String world, int start, int end) {
        if (end - start < 1) {
            return true;
        }
        if (!(new OffByOne().equalChars(world.charAt(start), world.charAt(end)))) {
            return false;
        }
        return isPalindromehelper(world, start + 1, end - 1);
    }
}
