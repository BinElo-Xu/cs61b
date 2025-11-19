package byog.Core;

public class Room{
    int x;
    int y;
    int width;
    int height;
    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Return true if this room overlap to other room.
     */
    public boolean isOverlapsTo(Room other) {
        int maxWidth = Math.max(width, other.width);
        int maxHeight = Math.max(height, other.height);
        return Math.abs(x - other.x) < maxWidth && Math.abs(y - other.y) < maxHeight;
    }
}
