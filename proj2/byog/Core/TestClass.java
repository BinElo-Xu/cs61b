package byog.Core;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestClass {
    @Test
    public void testIsOverlapsTo() {
        Room room = new Room(0, 0, 2, 4);
        Room otherRoom = new Room(2, 3, 2, 4);
        boolean expected = false;
        assertEquals(expected, room.isOverlapsTo(otherRoom));
        Room otherRoom2 = new Room(2, 4, 2, 4);
        boolean expected2 = true;
        assertEquals(expected, room.isOverlapsTo(otherRoom2));
        Room otherRoom3 = new Room(1, 3, 2, 4);
        assertEquals(expected2, room.isOverlapsTo(otherRoom3));
        Room otherRoom4 = new Room(0, 3, 2, 4);
        assertEquals(expected2, room.isOverlapsTo(otherRoom4));
    }
}
