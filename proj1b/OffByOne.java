public class OffByOne implements CharacterComparator {
    @Override
    public boolean equalChars(char x, char y) {
        int ans = Math.abs(x - y);
        return ans == 1;
    }
}
