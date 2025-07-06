public class OffByN implements CharacterComparator {
    private final int N;
    public OffByN(int N) {
        this.N = N;
    }
    @Override
    public boolean equalChars(char x, char y) {
        int diff = Math.abs(x - y);
        return diff == N;
    }
}
